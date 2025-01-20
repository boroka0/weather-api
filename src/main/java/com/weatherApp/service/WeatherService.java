package com.weatherApp.service;

import com.google.gson.Gson;
import com.weatherApp.model.WeatherAlert;
import com.weatherApp.model.WeatherAlertResponseDto;
import com.weatherApp.model.WeatherResponse;
import com.weatherApp.model.WeatherResponseDto;
import com.weatherApp.util.ConfigUtil;
import com.weatherApp.util.HttpClientUtil;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class WeatherService {

    public WeatherResponse getWeather(String city) throws Exception {
        String url = String.format("%s?q=%s&units=metric&appid=%s",
                ConfigUtil.get("api.base.url") + "/weather", city, ConfigUtil.get("api.key"));

        String responseJson = HttpClientUtil.get(url);

        WeatherResponseDto weather = new Gson().fromJson(responseJson, WeatherResponseDto.class);

        if (weather == null || weather.getName() == null) {
            throw new IllegalStateException("Invalid response from weather API.");
        }

        LocalTime sunrise = Instant.ofEpochSecond(weather.getSunrise())
                .atZone(ZoneId.systemDefault())
                .toLocalTime();

        LocalTime sunset = Instant.ofEpochSecond(weather.getSunset())
                .atZone(ZoneId.systemDefault())
                .toLocalTime();

        return new WeatherResponse(
                weather.getName(),
                weather.getMain().getTemp(),
                weather.getMain().getHumidity(),
                weather.getMain().getPressure(),
                weather.getWeather().get(0).getDescription(),
                LocalDate.now()
        );
    }

    public List<WeatherAlert> getWeatherAlerts(double lat, double lon) throws Exception {
        String url = String.format("%s/alerts.json?key=%s&q=%f,%f",
                ConfigUtil.get("weather.api.url"),
                ConfigUtil.get("weather.api.key"),
                lat,
                lon);

        String responseJson = HttpClientUtil.get(url);

        WeatherAlertResponseDto response = new Gson().fromJson(responseJson, WeatherAlertResponseDto.class);

        if (response == null || response.getAlerts().getAlert() == null || response.getAlerts().getAlert().isEmpty()) {
            return Collections.emptyList();
        }

        return response.getAlerts().getAlert().stream()
                .map(alertDto -> new WeatherAlert(
                        alertDto.getEvent(),
                        alertDto.getStartEpoch(),
                        alertDto.getEndEpoch(),
                        alertDto.getDescription()))
                .collect(Collectors.toList());
    }
}
