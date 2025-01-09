package com.weatherApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirQualityDto {
    private AirQualityMain main;
    private AirQualityComponents components;
    private long dt;
}
