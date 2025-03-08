package com.wilson.api_meteorologica.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wilson.api_meteorologica.DTO.AirQualityDTO;
import com.wilson.api_meteorologica.DTO.ErrorResponseDTO;
import com.wilson.api_meteorologica.DTO.WeatherDTO;
import com.wilson.api_meteorologica.DTO.WeatherForecastDTO;
import com.wilson.api_meteorologica.security.SecurityService;
import com.wilson.api_meteorologica.service.RateLimiterService;
import com.wilson.api_meteorologica.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherControllerTest {

    @Mock
    private WeatherService weatherService;

    @Mock
    private SecurityService securityService;

    @Mock
    private RateLimiterService rateLimiterService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private WeatherController weatherController;

    private final String CITY = "Bogotá";
    private final String USERNAME = "testUser";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);//abre e  inicializa los mocks

        // 🔥 Crear manualmente el controlador con la instancia mockeada de RateLimiterService
        weatherController = new WeatherController(rateLimiterService);
        ReflectionTestUtils.setField(weatherController, "weatherService", weatherService);
        ReflectionTestUtils.setField(weatherController, "securityService", securityService);
    }
    @Test
    void testRateLimiterMock() {
        when(rateLimiterService.tryConsume(USERNAME)).thenReturn(true);
        assertTrue(rateLimiterService.tryConsume(USERNAME), "El mock de RateLimiter debería devolver true");
    }

    @Test
    void testGetCurrentWeather_Success() {
        // Mock de usuario autenticado
        when(securityService.getAuthenticatedUser()).thenReturn(USERNAME);
        // Mock del Rate Limiter
        when(rateLimiterService.tryConsume(USERNAME)).thenReturn(true);
        // Mock de servicio de clima
        WeatherDTO mockWeather = new WeatherDTO();
        when(weatherService.getCurrentWeather(CITY)).thenReturn(mockWeather);

        ResponseEntity<?> response = weatherController.getCurrentWeather(CITY);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockWeather, response.getBody());
        verify(weatherService).registerQuery(USERNAME, "currentWeather", CITY, mockWeather);
    }

    @Test
    void testGetCurrentWeather_TooManyRequests() {
        when(securityService.getAuthenticatedUser()).thenReturn(USERNAME);
        when(rateLimiterService.tryConsume(USERNAME)).thenReturn(false);

        ResponseEntity<?> response = weatherController.getCurrentWeather(CITY);

        assertEquals(HttpStatus.TOO_MANY_REQUESTS, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponseDTO);
        verify(weatherService, never()).getCurrentWeather(anyString());
    }

    @Test
    void testGetWeatherForecast_Success() {
        when(securityService.getAuthenticatedUser()).thenReturn(USERNAME);
        when(rateLimiterService.tryConsume(USERNAME)).thenReturn(true);

        WeatherForecastDTO mockForecast = new WeatherForecastDTO();
        when(weatherService.getWeatherForecast(CITY)).thenReturn(mockForecast);

        ResponseEntity<?> response = weatherController.getWeatherForecast(CITY);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockForecast, response.getBody());
        verify(weatherService).registerQuery(USERNAME, "forecastWeather", CITY, mockForecast);
    }

    @Test
    void testGetWeatherForecast_TooManyRequests() {
        when(securityService.getAuthenticatedUser()).thenReturn(USERNAME);
        when(rateLimiterService.tryConsume(USERNAME)).thenReturn(false);

        ResponseEntity<?> response = weatherController.getWeatherForecast(CITY);

        assertEquals(HttpStatus.TOO_MANY_REQUESTS, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponseDTO);
        verify(weatherService, never()).getWeatherForecast(anyString());
    }

    @Test
    void testGetAirQuality_Success() {
        when(securityService.getAuthenticatedUser()).thenReturn(USERNAME);
        when(rateLimiterService.tryConsume(USERNAME)).thenReturn(true);

        AirQualityDTO mockAirQuality = new AirQualityDTO();
        when(weatherService.getAirQuality(CITY)).thenReturn(mockAirQuality);

        ResponseEntity<?> response = weatherController.getAirQuality(CITY);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockAirQuality, response.getBody());
        verify(weatherService).registerQuery(USERNAME, "air-qualityWeather", CITY, mockAirQuality);
    }

    @Test
    void testGetAirQuality_TooManyRequests() {
        when(securityService.getAuthenticatedUser()).thenReturn(USERNAME);
        when(rateLimiterService.tryConsume(USERNAME)).thenReturn(false);

        ResponseEntity<?> response = weatherController.getAirQuality(CITY);

        assertEquals(HttpStatus.TOO_MANY_REQUESTS, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponseDTO);
        verify(weatherService, never()).getAirQuality(anyString());
    }
}