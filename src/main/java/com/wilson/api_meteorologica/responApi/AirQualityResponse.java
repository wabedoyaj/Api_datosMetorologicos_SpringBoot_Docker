package com.wilson.api_meteorologica.responApi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AirQualityResponse {
    private List<AirData> list;

    @Getter @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AirData {
        private Main main;
        private Components components;

        @Getter @Setter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Main {
            private int aqi; // índice de calidad del aire
        }

        @Getter @Setter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Components {
            private double co;
            private double no;
            private double no2;
            private double o3;
            private double so2;
            private double pm2_5;
            private double pm10;
            private double nh3;
        }
    }
}
