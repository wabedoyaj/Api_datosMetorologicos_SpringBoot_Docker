package com.wilson.api_meteorologica.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
/**
 * clase DTO para el pronostico
 */

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class WeatherForecastDTO {

    private String city;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime queryDate;

    private List<ForecastDetail> Forecast; //pronostico

    @Getter @Setter
    public static class ForecastDetail { //detalle del pronostico
        private String dateTime;
        private String description;
        private double temperature;
        private double windchill; // sensacionTermica
        private int humidity;
        private double windKmh;  // velocidad del viento en Kmh
        private int probabilityRain; //probabilidad de Lluvia
        private double rainMm; //lluviaMm

        public ForecastDetail(String dateTime, String description,
                                 double temperature, double windchill, int humidity, double windKmh, int probabilityRain, double rainMm) {
            this.dateTime = dateTime;
            this.description = description;
            this.temperature = temperature;
            this.windchill = windchill;
            this.humidity = humidity;
            this.windKmh = windKmh * 3.6 ; // Convertimos de m/s a km/h
            this.probabilityRain = (probabilityRain * 100); //Convertimos de 0-1 a porcentaje
            this.rainMm = rainMm;
        }
    }
}
