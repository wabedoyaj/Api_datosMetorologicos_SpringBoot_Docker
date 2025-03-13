package com.wilson.api_meteorologica.controller;


import com.wilson.api_meteorologica.DTO.AirQualityDTO;
import com.wilson.api_meteorologica.DTO.WeatherDTO;
import com.wilson.api_meteorologica.DTO.WeatherForecastDTO;
import com.wilson.api_meteorologica.security.SecurityService;
import com.wilson.api_meteorologica.service.RateLimiterService;
import com.wilson.api_meteorologica.service.WeatherService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherControllerTest {

    @Mock
    private WeatherService weatherService;

    @Mock(lenient = true)
    private SecurityService securityService;

    @Mock
    private RateLimiterService rateLimiterService;

    @InjectMocks
    private WeatherController weatherController;

    private final String CITY = "Bogotá";
    private static  final String USERNAME = "testUser";
    private final String ENDPOINT_CURRENT = "currentWeather";
    private final String ENDPOINT_FORECAST = "forecastWeather";
    private final String ENDPOINT_AIR_QUALITY = "airQuality";
    @BeforeAll
    static void init() {
        MockitoAnnotations.openMocks(WeatherControllerTest.class);
    }

    @BeforeEach
    void setUp() {
       // Usa lenient() para evitar UnnecessaryStubbingException
        lenient().when(securityService.getAuthenticatedUser()).thenReturn(USERNAME);
        lenient().when(rateLimiterService.tryConsume(anyString(), anyString())).thenReturn(true); // Evita bloqueos inesperados
    }


    @Test
    void testRateLimiterMock() {
        // Llama al método para que el mock sea usado
        rateLimiterService.tryConsume(USERNAME, ENDPOINT_CURRENT);

        // Verifica que fue llamado al menos una vez
        verify(rateLimiterService, times(1)).tryConsume(USERNAME, ENDPOINT_CURRENT);
    }


    @Test
    void testGetCurrentWeather_Success() {
        // Mock de usuario autenticado dentro del test
        when(securityService.getAuthenticatedUser()).thenReturn(USERNAME);

        when(rateLimiterService.tryConsume(USERNAME,ENDPOINT_CURRENT)).thenReturn(true);
        WeatherDTO mockWeather = new WeatherDTO();
        when(weatherService.getCurrentWeather(CITY)).thenReturn(mockWeather);

        ResponseEntity<?> response = weatherController.getCurrentWeather(CITY);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockWeather, response.getBody());
    }

    @Test
    void testGetWeatherForecast_Success() {
        when(securityService.getAuthenticatedUser()).thenReturn(USERNAME);
        when(rateLimiterService.tryConsume(USERNAME, ENDPOINT_FORECAST)).thenReturn(true);

        WeatherForecastDTO mockForecast = new WeatherForecastDTO();
        when(weatherService.getWeatherForecast(CITY)).thenReturn(mockForecast);

        ResponseEntity<?> response = weatherController.getWeatherForecast(CITY);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockForecast, response.getBody());
    }

    @Test
    void testGetAirQuality_Success() {
        when(securityService.getAuthenticatedUser()).thenReturn(USERNAME);
        when(rateLimiterService.tryConsume(USERNAME, ENDPOINT_AIR_QUALITY)).thenReturn(true);

        AirQualityDTO mockAirQuality = new AirQualityDTO();
        when(weatherService.getAirQuality(CITY)).thenReturn(mockAirQuality);

        ResponseEntity<?> response = weatherController.getAirQuality(CITY);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockAirQuality, response.getBody());
    }

    @Test
    void testGetCurrentWeather_TooManyRequests() {
        lenient().when(securityService.getAuthenticatedUser()).thenReturn(USERNAME);
        lenient().when(rateLimiterService.tryConsume(USERNAME, ENDPOINT_CURRENT)).thenReturn(false);
        lenient().when(rateLimiterService.getTimeUntilReset(USERNAME, ENDPOINT_CURRENT)).thenReturn(30L);

        ResponseEntity<?> response = weatherController.getCurrentWeather(CITY);

        assertEquals(HttpStatus.TOO_MANY_REQUESTS, response.getStatusCode());
        assertInstanceOf(Map.class, response.getBody());
        assertTrue(((Map<?, ?>) response.getBody()).containsKey("wait_time_seconds"));
        verify(weatherService, never()).getCurrentWeather(anyString());
    }

    @Test
    void testGetWeatherForecast_TooManyRequests() {
        lenient().when(securityService.getAuthenticatedUser()).thenReturn(USERNAME);
        lenient().when(rateLimiterService.tryConsume(USERNAME, ENDPOINT_FORECAST)).thenReturn(false);
        lenient().when(rateLimiterService.getTimeUntilReset(USERNAME, ENDPOINT_FORECAST)).thenReturn(45L);

        ResponseEntity<?> response = weatherController.getWeatherForecast(CITY);

        assertEquals(HttpStatus.TOO_MANY_REQUESTS, response.getStatusCode());
        assertInstanceOf(Map.class, response.getBody());
        assertTrue(((Map<?, ?>) response.getBody()).containsKey("wait_time_seconds"));
        verify(weatherService, never()).getWeatherForecast(anyString());
    }
    @Test
    void testGetAirQuality_TooManyRequests() {
        lenient().when(securityService.getAuthenticatedUser()).thenReturn(USERNAME);
        lenient().when(rateLimiterService.tryConsume(USERNAME, ENDPOINT_AIR_QUALITY)).thenReturn(false);
        lenient().when(rateLimiterService.getTimeUntilReset(USERNAME, ENDPOINT_AIR_QUALITY)).thenReturn(60L);

        ResponseEntity<?> response = weatherController.getAirQuality(CITY);

        assertEquals(HttpStatus.TOO_MANY_REQUESTS, response.getStatusCode());
        assertInstanceOf(Map.class, response.getBody());
        assertTrue(((Map<?, ?>) response.getBody()).containsKey("wait_time_seconds"));
        verify(weatherService, never()).getAirQuality(anyString());
    }
}
