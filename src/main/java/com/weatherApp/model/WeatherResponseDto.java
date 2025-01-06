package com.weatherApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherResponseDto {
    private String name;
    private Main main;
    private List<Weather> weather;
}