package com.weatherApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeocodingResponse {
    private String name;
    private double lat;
    private double lon;
    private String country;
    private String state;
}