package com.weatherApp.service;

import com.google.gson.Gson;
import com.weatherApp.model.*;
import com.weatherApp.util.ConfigUtil;
import com.weatherApp.util.HttpClientUtil;
import com.weatherApp.util.TranslatorUtil;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

public class WeatherService {

    public WeatherResponse getWeather(String city) throws Exception {
        String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);

        String url = String.format("%s?q=%s&units=metric&appid=%s",
                ConfigUtil.get("api.base.url") + "/weather", encodedCity, ConfigUtil.get("api.key"));

        String responseJson = HttpClientUtil.get(url);

        WeatherResponseDto weather = new Gson().fromJson(responseJson, WeatherResponseDto.class);

        if (weather == null || weather.getName() == null) {
            throw new IllegalStateException("Invalid response from weather API.");
        }

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
        String url = String.format("%s/alerts.json?key=%s&q=%f,%f&lang=en",
                ConfigUtil.get("weather.api.url"),
                ConfigUtil.get("weather.api.key"),
                lat,
                lon);

        String responseJson = HttpClientUtil.get(url);

        WeatherAlertResponseDto response = new Gson().fromJson(responseJson, WeatherAlertResponseDto.class);

        if (response == null || response.getAlerts().getAlert() == null || response.getAlerts().getAlert().isEmpty()) {
            return Collections.emptyList();
        }

        Set<String> uniqueEvents = new HashSet<>();
        List<WeatherAlert> alerts = new ArrayList<>();

        for (WeatherAlertDto alertDto : response.getAlerts().getAlert()) {
            String translatedEvent = translateAlert(alertDto.getEvent());

            if (!uniqueEvents.contains(translatedEvent)) {
                uniqueEvents.add(translatedEvent);
                alerts.add(new WeatherAlert(
                        translatedEvent,
                        alertDto.getStartEpoch(),
                        alertDto.getEndEpoch(),
                        translateAlert(alertDto.getDescription())
                ));
            }
        }
        return alerts;
    }

    private String translateAlert(String alertText) {
        return TranslatorUtil.translateTextToEnglish(alertText);
    }
}
