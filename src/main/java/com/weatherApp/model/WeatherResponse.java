package com.weatherApp.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class WeatherResponse {
    private String cityName;
    private double temperature;
    private double humidity;
    private double pressure;
    private String weatherDescription;
    private LocalDate date;
}