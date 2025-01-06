package com.weatherApp.service;

import com.google.gson.Gson;
import com.weatherApp.model.WeatherResponse;
import com.weatherApp.model.WeatherResponseDto;
import com.weatherApp.util.ConfigUtil;
import com.weatherApp.util.HttpClientUtil;

import java.time.LocalDate;

public class WeatherService {

    public WeatherResponse getWeather(String city) throws Exception {
        String url = String.format("%s?q=%s&units=metric&appid=%s",
                ConfigUtil.get("api.base.url") + "/weather", city, ConfigUtil.get("api.key"));

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
}
