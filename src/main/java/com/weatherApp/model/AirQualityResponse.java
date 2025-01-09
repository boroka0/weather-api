package com.weatherApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirQualityResponse {

    private Location location;
    private AirQuality airQuality;
    private long timestamp;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Location {
        private double latitude;
        private double longitude;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AirQuality {
        private int aqi;
        private AirQualityComponents components;
        private String advice;
    }
}
