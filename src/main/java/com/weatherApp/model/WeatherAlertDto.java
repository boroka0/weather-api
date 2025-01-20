package com.weatherApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherAlertDto {
    private String event;
    private long startEpoch;
    private long endEpoch;
    private String description;
}