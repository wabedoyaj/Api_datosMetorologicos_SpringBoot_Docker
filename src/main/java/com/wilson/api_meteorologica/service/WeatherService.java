package com.wilson.api_meteorologica.service;


import com.wilson.api_meteorologica.DTO.AirQualityDTO;
import com.wilson.api_meteorologica.DTO.WeatherDTO;
import com.wilson.api_meteorologica.DTO.WeatherForecastDTO;
import com.wilson.api_meteorologica.responApi.AirQualityResponse;
import com.wilson.api_meteorologica.responApi.WeatherForecastResponse;
import com.wilson.api_meteorologica.responApi.WeatherResponse;
import com.wilson.api_meteorologica.security.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;



import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio que gestiona las consultas meteorológicas utilizando la API de OpenWeatherMap.
 * Proporciona datos del clima actual, pronóstico y calidad del aire.
 */
@Service
public class WeatherService {
    @Value("${openweathermap.api.key}")
    private String apiKey;
    @Autowired
    private AuditService auditService;
    @Autowired
    private SecurityService securityService;
    private final WebClient webClient;
    private static final Logger logger =  LoggerFactory.getLogger(WeatherService.class);
    @Autowired
    public WeatherService(@Value("${openweathermap.api.base-url}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)  // Usa la URL desde application.properties
                .build();
    }

    /**
     * Obtiene el clima actual de una ciudad desde OpenWeatherMap.
     * Los resultados se almacenan en caché para mejorar el rendimiento.
     * @param city Nombre de la ciudad a consultar.
     * @return Objeto WeatherDTO con la información del clima.
     */
    @Cacheable("currentWeather") // Caché para el clima actual
    public WeatherDTO getCurrentWeather(String city) {
        WeatherResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/weather")
                        .queryParam("q", city)
                        .queryParam("appid", apiKey)
                        .queryParam("units", "metric")
                        .build())
                .retrieve()
                .bodyToMono(WeatherResponse.class)
                .block();
        WeatherDTO weatherDTO =  new WeatherDTO(
                response.getName(),
                response.getWeather().get(0).getDescription(),
                response.getMain().getTemp(),
                response.getMain().getHumidity(),
                response.getWind().getSpeed(),
                LocalDateTime.now()
        );
        // Obtener usuario autenticado desde SecurityService
        String username = securityService.getAuthenticatedUser();
        // Registrar auditoría con el usuario autenticado
        auditService.registerQuery(username, "currentWeather", city, weatherDTO);

        return weatherDTO;
    }
    /**
     * Obtiene el pronóstico del clima para los próximos 5 días.
     * Los datos se almacenan en caché para reducir llamadas innecesarias a la API.
     * @param city Nombre de la ciudad a consultar.
     * @return Objeto WeatherForecastDTO con la información del pronóstico.
     */
    @Cacheable("forecastWeather") // Caché para el pronóstico
    public WeatherForecastDTO getWeatherForecast(String city) {
        WeatherForecastResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/forecast")
                        .queryParam("q", city)
                        .queryParam("appid", apiKey)
                        .queryParam("units", "metric")
                        .build())
                .retrieve()
                .bodyToMono(WeatherForecastResponse.class)
                .block();
        //  // Convertir la respuesta en una lista de detalles del pronóstico
        List<WeatherForecastDTO.ForecastDetail> forecastList = response.getList().stream().map(forecast -> {
            double rainMm = forecast.getRain() != null ? forecast.getRain().get_3h() : 0.0;
            return new WeatherForecastDTO.ForecastDetail(
                    forecast.getDt_txt(),
                    forecast.getWeather().get(0).getDescription(),
                    forecast.getMain().getTemp(),
                    forecast.getMain().getFeels_like(),
                    forecast.getMain().getHumidity(),
                    forecast.getWind().getSpeed() * 3.6, // Convertimos de m/s a km/h
                    (int) (forecast.getPop() * 100), // Convertimos de 0-1 a porcentaje
                    rainMm
            );
        }).collect(Collectors.toList());
        WeatherForecastDTO forecastDTO = new WeatherForecastDTO(response.getCity().getName(), LocalDateTime.now(), forecastList);
        // Registrar auditoría con el usuario autenticado
        auditService.registerQuery(securityService.getAuthenticatedUser(), "forecastWeather", city, forecastDTO);
        return forecastDTO;
    }
    /**
     * Obtiene información sobre la calidad del aire en una ciudad específica.
     * Primero obtiene las coordenadas de la ciudad y luego consulta la API de contaminación.
     * Los datos se almacenan en caché para evitar consultas repetidas.
     *
     * @param city Nombre de la ciudad a consultar.
     * @return Objeto AirQualityDTO con la información sobre la calidad del aire.
     */
    @Cacheable("airQuality") // Caché para la contaminación del aire
    public AirQualityDTO getAirQuality(String city) {
        // Obtener coordenadas geográficas de la ciudad
        WeatherResponse geoResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/weather")
                        .queryParam("q", city)
                        .queryParam("appid", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(WeatherResponse.class)
                .block();

        double lat = geoResponse.getCoord().getLat();
        double lon = geoResponse.getCoord().getLon();

        // Obtener calidad del aire con las coordenadas
        AirQualityResponse airResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/air_pollution")
                        .queryParam("lat", lat)
                        .queryParam("lon", lon)
                        .queryParam("appid", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(AirQualityResponse.class)
                .block();

        AirQualityResponse.AirData airData = airResponse.getList().get(0);
        AirQualityDTO airQualityDTO = new AirQualityDTO(
                city, LocalDateTime.now(),
                new AirQualityDTO.AirQuality(airData.getMain().getAqi()),
                new AirQualityDTO.Pollutant(
                        airData.getComponents().getCo(),
                        airData.getComponents().getNo(),
                        airData.getComponents().getNo2(),
                        airData.getComponents().getO3(),
                        airData.getComponents().getSo2(),
                        airData.getComponents().getPm2_5(),
                        airData.getComponents().getPm10(),
                        airData.getComponents().getNh3()
                )
        );
        // Registrar auditoría con el usuario autenticado
        auditService.registerQuery(securityService.getAuthenticatedUser(), "airQuality", city, airQualityDTO);
        return airQualityDTO;
    }


}
