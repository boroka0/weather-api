package com.weatherApp.service;

import com.google.gson.Gson;
import com.weatherApp.model.ForecastResponseDto;
import com.weatherApp.model.ForecastWeatherDto;
import com.weatherApp.model.WeatherResponse;
import com.weatherApp.util.ConfigUtil;
import com.weatherApp.util.HttpClientUtil;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class ForecastService {

    public List<WeatherResponse> getForecast(String city) {
        String responseJson;

        String url = String.format("%s/forecast?q=%s&units=metric&appid=%s",
                ConfigUtil.get("api.base.url"),
                city,
                ConfigUtil.get("api.key"));

        System.out.println("Forecast API URL: " + url);

        try {
            responseJson = HttpClientUtil.get(url);
        } catch (IOException e) {
            System.err.println("Error Response from API: " + e.getMessage());
            throw new RuntimeException(e);
        }

        ForecastResponseDto forecastDto = new Gson().fromJson(responseJson, ForecastResponseDto.class);
        List<WeatherResponse> forecast = new ArrayList<>();

        if (forecastDto == null || forecastDto.getList() == null) {
            throw new IllegalStateException("Invalid response from forecast API.");
        }

        for (ForecastWeatherDto day : forecastDto.getList()) {

            LocalDate date = Instant.ofEpochSecond(day.getDt())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            LocalTime sunrise = Instant.ofEpochSecond(forecastDto.getCity().getSunrise())
                    .atZone(ZoneId.systemDefault())
                    .toLocalTime();

            LocalTime sunset = Instant.ofEpochSecond(forecastDto.getCity().getSunset())
                    .atZone(ZoneId.systemDefault())
                    .toLocalTime();

            WeatherResponse weatherResponse = new WeatherResponse(
                    city,
                    day.getMain().getTemp(),
                    day.getMain().getHumidity(),
                    day.getMain().getPressure(),
                    day.getWeather().get(0).getDescription(),
                    date,
                    sunrise,
                    sunset
            );
            forecast.add(weatherResponse);
        }
        return forecast;
    }
}
