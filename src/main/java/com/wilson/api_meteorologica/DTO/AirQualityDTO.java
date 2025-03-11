package com.wilson.api_meteorologica.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * DTO para representar la calidad del aire en una ciudad específica.
 * Contiene información sobre el índice de calidad del aire (AQI),
 * contaminantes y la fecha de la consulta.
 */
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class AirQualityDTO {

    private String city;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateConsultation;
    private AirQuality airquality;// Índice de calidad del aire
    private Pollutant pollutants;// Contaminantes en el aire

    /**
     * Clase anidada que representa el índice de calidad del aire (AQI).
     * Categoriza el nivel de contaminación basado en el índice.
     */
    @Getter
    public static class AirQuality{
        private int index; // Índice de calidad del aire (AQI
        private String category; // // Categoría de contaminación (Bueno, Aceptable, etc.)

        public AirQuality(int index) {
            this.index = index;
            this.category = categorizarAQI(index);
        }

        /**
         * Método que clasifica el índice AQI en categorías de contaminación.
         * @param aqi Índice de calidad del aire (1-5).
         * @return Categoría correspondiente al AQI.
         */
        private String categorizarAQI(int aqi) {
            return switch (aqi) {
                case 1 -> "Bueno";
                case 2 -> "Aceptable";
                case 3 -> "Moderado";
                case 4 -> "Malo";
                case 5 -> "Peligroso";
                default -> "Desconocido";
            };
        }

    }

    /**
     * Clase anidada que representa los contaminantes en el aire.
     * Contiene mediciones de distintos gases y partículas en suspensión.
     */
    @Getter
    public static class Pollutant{
        private double CO, NO, NO2, O3, SO2, PM25, PM10, NH3; // Concentración de cada contaminante

        public Pollutant(double co, double no, double no2, double o3, double so2, double pm25, double pm10, double nh3) {
            this.CO = co;
            this.NO = no;
            this.NO2 = no2;
            this.O3 = o3;
            this.SO2 = so2;
            this.PM25 = pm25;
            this.PM10 = pm10;
            this.NH3 = nh3;
        }

    }
}
