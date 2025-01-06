package com.weatherApp.controller;

import com.weatherApp.model.WeatherResponse;
import com.weatherApp.service.ForecastService;
import com.weatherApp.service.WeatherService;

import java.util.List;

public class WeatherController {

    private final WeatherService weatherService;
    private final ForecastService forecastService;

    public WeatherController() {
        this.weatherService = new WeatherService();
        this.forecastService = new ForecastService();
    }

    public void getWeather(String city) {
        try {
            WeatherResponse response = weatherService.getWeather(city);
            displayWeather(response);
        } catch (Exception e) {
            handleError(e);
        }
    }

    private void displayWeather(WeatherResponse response) {
        System.out.println("Weather in " + response.getCityName() + ":");
        System.out.println("Temperature: " + response.getTemperature() + "°C");
        System.out.println("Humidity: " + response.getHumidity() + "%");
        System.out.println("Pressure: " + response.getPressure() + " hPa");
        System.out.println("Description: " + response.getWeatherDescription());
    }

    private void handleError(Exception e) {
        System.err.println("Error fetching weather data: " + e.getMessage());
        if (e.getCause() != null) {
            System.err.println("Cause: " + e.getCause().getMessage());
        }
    }

    public void getForecast(String city) {
        try {
            List<WeatherResponse> forecast = forecastService.getForecast(city);
            displayForecast(forecast);
        } catch (Exception e) {
            handleError(e);
        }
    }

    private void displayForecast(List<WeatherResponse> forecast) {
        System.out.println("Weather forecast for the next " + forecast.size() / 8 + " days:");
        for (WeatherResponse response : forecast) {
            System.out.println("Date: " + response.getDate());
            System.out.println("Temperature: " + response.getTemperature() + "°C");
            System.out.println("Humidity: " + response.getHumidity() + "%");
            System.out.println("Pressure: " + response.getPressure() + " hPa");
            System.out.println("Description: " + response.getWeatherDescription());
            System.out.println("-----------");
        }
    }
}
