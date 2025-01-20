package com.weatherApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherAlert {
    private String event;
    private long start;
    private long end;
    private String description;
}
