package com.weatherApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherAlertResponseDto {
    private AlertWrapper alerts;

    @Data
    public static class AlertWrapper {
        private List<WeatherAlertDto> alert;
    }
}
