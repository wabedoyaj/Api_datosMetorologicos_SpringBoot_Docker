package com.wilson.api_meteorologica.responApi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherForecastResponse {
    private City city;
    private List<Forecast> list;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class City {
        private String name;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Forecast {
        private String dt_txt;
        private Main main;
        private List<Weather> weather;
        private Wind wind;
        private Rain rain;
        private double pop; // probabilidad de lluvia

        @Getter
        @Setter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Main {
            private double temp;
            private double feels_like;
            private int humidity;
        }

        @Getter
        @Setter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Weather {
            private String description;
        }

        @Getter
        @Setter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Wind {
            private double speed; // en m/s
        }

        @Getter
        @Setter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Rain {
            private double _3h; // cantidad de lluvia en mm (puede ser nulo)
        }
    }
}
