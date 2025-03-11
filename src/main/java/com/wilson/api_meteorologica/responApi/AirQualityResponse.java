package com.wilson.api_meteorologica.responApi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
/**
 * Representa la respuesta de la API de calidad del aire.
 * Contiene una lista de datos sobre la calidad del aire en una ubicación específica.
 */
@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AirQualityResponse {
    private List<AirData> list;

    /**
     * Contiene los datos principales de la calidad del aire.
     */
    @Getter @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AirData {
        private Main main;  // Información general (ejemplo: índice AQI)
        private Components components; // Niveles de contaminantes

        /**
         * Representa el índice de calidad del aire (AQI).
         */
        @Getter @Setter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Main {
            private int aqi; // índice de calidad del aire
        }

        /**
         * Contiene las concentraciones de diferentes contaminantes en el aire.
         */
        @Getter @Setter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Components {
            private double co;    // Monóxido de carbono (CO)
            private double no;    // Óxido nítrico (NO)
            private double no2;   // Dióxido de nitrógeno (NO2)
            private double o3;    // Ozono (O3)
            private double so2;   // Dióxido de azufre (SO2)
            private double pm2_5; // Partículas finas PM2.5
            private double pm10;  // Partículas gruesas PM10
            private double nh3;   // Amoníaco (NH3)
        }
    }
}
