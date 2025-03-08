package com.wilson.api_meteorologica.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class AirQualityDTO {
    private String city;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateConsultation;
    private AirQuality airquality;
    private Pollutant pollutants;


    @Getter
    public static class AirQuality{
        private int index; //indice
        private String category; // categoria
        public AirQuality(int index) {
            this.index = index;
            this.category = categorizarAQI(index);
        }

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
    @Getter
    public static class Pollutant{
        private double CO, NO, NO2, O3, SO2, PM25, PM10, NH3;

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
