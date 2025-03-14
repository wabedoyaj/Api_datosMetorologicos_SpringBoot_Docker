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
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
    @Autowired
    private CacheManager cacheManager;
    private static final Logger logger =  LoggerFactory.getLogger(WeatherService.class);

    public WeatherService(@Value("${openweathermap.api.base-url}") String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build(); // Usa la URL desde application.properties
    }
    /**
     * Obtiene el clima actual de una ciudad desde OpenWeatherMap.
     * Los resultados se almacenan en caché para mejorar el rendimiento.
     * @param city Nombre de la ciudad a consultar.
     * @return Objeto WeatherDTO con la información del clima.
     */
    @Cacheable(value = "currentWeather", key = "#city") // Caché para el clima actual
    public WeatherDTO getCurrentWeatherFromApi(String city) {
        WeatherResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/weather")
                        .queryParam("q", city)
                        .queryParam("appid", apiKey)
                        .queryParam("units", "metric")
                        .build())
                .retrieve()
                .bodyToMono(WeatherResponse.class)
                .block();

        WeatherDTO weatherDTO = new WeatherDTO(
                response.getName(),
                response.getWeather().get(0).getDescription(),
                response.getMain().getTemp(),
                response.getMain().getHumidity(),
                response.getWind().getSpeed(),
                LocalDateTime.now()
        );
        return weatherDTO;
    }
    /**
     * Obtiene el pronóstico del clima para los próximos 5 días.
     * Los datos se almacenan en caché para reducir llamadas innecesarias a la API.
     * @param city Nombre de la ciudad a consultar.
     * @return Objeto WeatherForecastDTO con la información del pronóstico.
     */
    @Cacheable(value = "forecastWeather", key = "#city") // Caché para el pronóstico
    public WeatherForecastDTO getWeatherForecastFromApi(String city) {
        WeatherForecastResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/forecast")
                        .queryParam("q", city)
                        .queryParam("appid", apiKey)
                        .queryParam("units", "metric")
                        .build())
                .retrieve()
                .bodyToMono(WeatherForecastResponse.class)
                .block();
        // Usamos un LinkedHashMap para mantener el orden y evitar duplicados
        Map<LocalDate, WeatherForecastDTO.ForecastDetail> filteredForecast = new LinkedHashMap<>();

        for (WeatherForecastResponse.Forecast forecast : response.getList()) {
            LocalDate date = LocalDate.parse(forecast.getDt_txt().substring(0, 10)); // Extraer solo la fecha (YYYY-MM-DD)

            // Si aún no tenemos datos para este día, lo agregamos
            if (!filteredForecast.containsKey(date)) {
                double rainMm = forecast.getRain() != null ? forecast.getRain().get_3h() : 0.0;

                WeatherForecastDTO.ForecastDetail forecastDetail = new WeatherForecastDTO.ForecastDetail(
                        forecast.getDt_txt(),
                        forecast.getWeather().get(0).getDescription(),
                        forecast.getMain().getTemp(),
                        forecast.getMain().getFeels_like(),
                        forecast.getMain().getHumidity(),
                        forecast.getWind().getSpeed() * 3.6, // Convertimos m/s a km/h
                        (int) (forecast.getPop() * 100), // Convertimos de 0-1 a porcentaje
                        rainMm
                );

                filteredForecast.put(date, forecastDetail);
            }
        }
        // Convertimos el mapa a una lista y limitamos a 5 días
        List<WeatherForecastDTO.ForecastDetail> forecastList = filteredForecast.values()
                .stream()
                .limit(5)
                .collect(Collectors.toList());
        // Crear objeto de respuesta
        WeatherForecastDTO forecastDTO = new WeatherForecastDTO(response.getCity().getName(), LocalDateTime.now(), forecastList);
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
    @Cacheable(value = "airQuality", key = "#city") // Caché para la contaminación del aire
    public AirQualityDTO getAirQualityFromApi(String city) {
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
        return airQualityDTO;
    }

    public WeatherForecastDTO getWeatherForecast(String city) {
        WeatherForecastDTO forecast = getWeatherForecastFromApi(city);
        auditService.registerQuery("forecastWeather", city, forecast);
        return forecast;
    }

    public WeatherDTO getCurrentWeather(String city) {
        WeatherDTO weather = getCurrentWeatherFromApi(city);
        auditService.registerQuery("currentWeather", city, weather);
        return weather;
    }
    public AirQualityDTO getAirQuality(String city) {
        AirQualityDTO airQuality = getAirQualityFromApi(city);
        auditService.registerQuery("airQuality", city, airQuality);
        return airQuality;
    }


}
