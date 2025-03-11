package com.wilson.api_meteorologica.responApi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
/**
 * Modelo de respuesta para datos meteorológicos obtenidos de la API de OpenWeather.
 * Mapea la estructura de datos recibida en la API externa.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter @Setter
public class WeatherResponse {
    private String name;
    /** Lista de condiciones meteorológicas (ejemplo: lluvia, nublado, soleado) */
    private List<Weather> weather;
    /** Información principal del clima (temperatura, humedad) */
    private Main main;
    /** Datos sobre el viento (velocidad) */
    private Wind wind;
    /** Coordenadas geográficas de la ciudad */
    private Coord coord;

   // Clases internas para mapear datos de OpenWeather
    /**
     * Representa el estado del clima (ejemplo: "nublado", "lluvioso").
     */
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Weather {
        private String description;
    }

    /**
     * Contiene datos principales del clima.
     */
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Main {
        private double temp;
        private int humidity;
    }

    /**
     * Representa la velocidad del viento.
     */
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Wind {
        private double speed;
    }

    /**
     * Contiene las coordenadas geográficas de la ubicación consultada.
     */
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Coord {
        private double lat;
        private double lon;
    }
}
