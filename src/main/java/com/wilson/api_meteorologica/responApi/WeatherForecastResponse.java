package com.wilson.api_meteorologica.responApi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
/**
 * Representa la respuesta de la API de pronóstico del tiempo.
 * Contiene información sobre la ciudad y una lista de pronósticos detallados.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherForecastResponse {
    private City city;
    private List<Forecast> list;

    /**
     * Representa la información de la ciudad del pronóstico.
     */
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class City {
        private String name;
    }

    /**
     * Representa un pronóstico del clima en un momento específico.
     */
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Forecast {
        private String dt_txt; // Fecha y hora del pronóstico
        private Main main; // Datos principales del clima
        private List<Weather> weather; // Condiciones meteorológicas
        private Wind wind; // Información sobre el viento
        private Rain rain; // Información sobre la lluvia (opcional)
        private double pop; // Probabilidad de precipitación (%)

        /**
         * Contiene datos de temperatura y humedad.
         */
        @Getter
        @Setter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Main {
            private double temp; //Temperatura en grados Celsius
            private double feels_like; // Sensación térmica en grados Celsius
            private int humidity; // Humedad relativa en porcentaje
        }

        /**
         * Describe las condiciones meteorológicas generales.
         */
        @Getter
        @Setter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Weather {
            private String description;
        }

        /**
         * Contiene información sobre la velocidad del viento.
         */
        @Getter
        @Setter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Wind {
            private double speed; // Velocidad del viento en metros por segundo (m/s)
        }

        /**
         * Representa la cantidad de lluvia en las últimas 3 horas.
         */
        @Getter
        @Setter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Rain {
            private double _3h; // cantidad de lluvia en mm (puede ser nulo si no llueve)
        }
    }
}
