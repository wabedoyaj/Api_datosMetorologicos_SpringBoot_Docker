package com.wilson.api_meteorologica.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wilson.api_meteorologica.DTO.AirQualityDTO;
import com.wilson.api_meteorologica.DTO.WeatherDTO;
import com.wilson.api_meteorologica.DTO.WeatherForecastDTO;
import com.wilson.api_meteorologica.entity.AuditConsultation;
import com.wilson.api_meteorologica.repository.AuditConsultatiorepository;
import com.wilson.api_meteorologica.responApi.AirQualityResponse;
import com.wilson.api_meteorologica.responApi.WeatherForecastResponse;
import com.wilson.api_meteorologica.responApi.WeatherResponse;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class WeatherService {
    @Value("${openweathermap.api.key}")
    private String apiKey;
    @Autowired
    private AuditConsultatiorepository consultatiorepository;
    @Autowired
    private ObjectMapper objectMapper; // Se inyecta el ObjectMapper
    private final WebClient webClient = WebClient.builder().baseUrl("https://api.openweathermap.org/data/2.5").build();
    private static final Logger logger =  LoggerFactory.getLogger(WeatherService.class);

    @Cacheable("currentWeather") // Caché para el clima actual
    public WeatherDTO getCurrentWeather(String city) {
        System.out.println("Llamando a la API externa para obtener datos de: " + city);
        WeatherResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/weather")
                        .queryParam("q", city)
                        .queryParam("appid", apiKey)
                        .queryParam("units", "metric")
                        .build())
                .retrieve()
                .bodyToMono(WeatherResponse.class)
                .block();
        return new WeatherDTO(
                response.getName(),
                response.getWeather().get(0).getDescription(),
                response.getMain().getTemp(),
                response.getMain().getHumidity(),
                response.getWind().getSpeed(),
                LocalDateTime.now()
        );

    }
    @Cacheable("forecastWeather") // Caché para el pronóstico
    public WeatherForecastDTO getWeatherForecast(String city) {
        System.out.println("Llamando a la API externa para obtener datos de: " + city);
        WeatherForecastResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/forecast")
                        .queryParam("q", city)
                        .queryParam("appid", apiKey)
                        .queryParam("units", "metric")
                        .build())
                .retrieve()
                .bodyToMono(WeatherForecastResponse.class)
                .block();
        // Convertir la respuesta a WeatherForecastDTO
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

        return new WeatherForecastDTO(response.getCity().getName(), LocalDateTime.now(), forecastList);
    }
    @Cacheable("airQuality") // Caché para la contaminación del aire
    public AirQualityDTO getAirQuality(String city) {
        System.out.println("Llamando a la API externa para obtener datos de: " + city);
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
        AirQualityDTO.AirQuality airQuality = new AirQualityDTO.AirQuality(airData.getMain().getAqi());

        AirQualityDTO.Pollutant pollutants = new AirQualityDTO.Pollutant(
                airData.getComponents().getCo(),
                airData.getComponents().getNo(),
                airData.getComponents().getNo2(),
                airData.getComponents().getO3(),
                airData.getComponents().getSo2(),
                airData.getComponents().getPm2_5(),
                airData.getComponents().getPm10(),
                airData.getComponents().getNh3()
        );

        return new AirQualityDTO(city, LocalDateTime.now(), airQuality, pollutants);
    }
    @Transactional
    public void registerQuery(String username, String queryType, String city, Object responseObject) {
        logger.info("Intentando guardar consulta en la base de datos...");
        logger.info("Usuario: {} | Tipo: {} | Ciudad: {}", username, queryType, city);
        String responseJson;
        try {
            responseJson = objectMapper.writeValueAsString(responseObject);
        } catch (JsonProcessingException e) {
            responseJson = "{}";
        }
        AuditConsultation query = new AuditConsultation(username,queryType,city, LocalDateTime.now(),responseJson);
        consultatiorepository.save(query);
        consultatiorepository.flush(); // 🔹 Forzar escritura inmediata en la BD
        logger.info("✅ Consulta guardada correctamente.");
    }
}
