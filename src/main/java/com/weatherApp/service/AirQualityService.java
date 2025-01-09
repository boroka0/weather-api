package com.weatherApp.service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.weatherApp.model.AirQualityDto;
import com.weatherApp.model.AirQualityResponse;
import com.weatherApp.model.AirQualityResponseDto;
import com.weatherApp.util.ConfigUtil;
import com.weatherApp.util.HttpClientUtil;

import java.io.IOException;

public class AirQualityService {

    public AirQualityResponse getAirQuality(double lat, double lon) {
        String url = String.format("%s/air_pollution?lat=%f&lon=%f&appid=%s",
                ConfigUtil.get("api.base.url"),
                lat,
                lon,
                ConfigUtil.get("api.key"));

        String responseJson;

        try {
            responseJson = HttpClientUtil.get(url);
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch air quality data.", e);
        }

        AirQualityResponseDto dto;
        try {
            dto = new Gson().fromJson(responseJson, AirQualityResponseDto.class);
        } catch (JsonSyntaxException e) {
            throw new IllegalStateException("Failed to parse air quality response JSON.", e);
        }

        if (dto == null || dto.getList() == null || dto.getList().isEmpty()) {
            throw new IllegalStateException("Invalid response structure from Air Quality API.");
        }

        AirQualityDto firstEntry = dto.getList().get(0);
        return mapToAirQualityResponse(lat, lon, firstEntry);
    }

    private AirQualityResponse mapToAirQualityResponse(double lat, double lon, AirQualityDto dto) {
        AirQualityResponse.AirQuality airQuality = new AirQualityResponse.AirQuality(
                dto.getMain().getAqi(),
                dto.getComponents(),
                getHealthAdvice(dto.getMain().getAqi())
        );

        return new AirQualityResponse(
                new AirQualityResponse.Location(lat, lon),
                airQuality,
                dto.getDt()
        );
    }

    private String getHealthAdvice(int aqi) {
        return switch (aqi) {
            case 1 -> "Air quality is ideal. No action needed.";
            case 2 -> "Moderate air quality; sensitive groups should limit prolonged outdoor activity.";
            case 3 -> "Unhealthy for sensitive groups; reduce outdoor exertion.";
            case 4 -> "Everyone should limit outdoor activity.";
            case 5 -> "Serious health effects; avoid outdoor activity.";
            default -> "No advisory available.";
        };
    }
}