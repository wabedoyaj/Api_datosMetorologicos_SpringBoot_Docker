package com.wilson.api_meteorologica.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
/**
 * DTO para representar los datos del clima en una ciudad.
 * Se utiliza para enviar la información meteorológica en las respuestas de la API.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WeatherDTO {
    private String city;

    private String description;

    private double temperature;

    private int humidity;

    private double speedWind;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateQuery;


}
