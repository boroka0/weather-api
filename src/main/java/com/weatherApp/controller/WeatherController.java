package com.weatherApp.controller;

import com.weatherApp.model.AirQualityResponse;
import com.weatherApp.model.Location;
import com.weatherApp.model.WeatherAlert;
import com.weatherApp.model.WeatherResponse;
import com.weatherApp.service.*;

import java.util.List;

public class WeatherController {

    private final WeatherService weatherService;
    private final ForecastService forecastService;
    private final AirQualityService airQualityService;
    private final GeocodingService geocodingService;

    private final UserPreferencesService userPreferencesService;


    public WeatherController() {
        this.weatherService = new WeatherService();
        this.forecastService = new ForecastService();
        this.airQualityService = new AirQualityService();
        this.geocodingService = new GeocodingService();
        this.userPreferencesService = new UserPreferencesService();
    }

    public void getWeather(String city) {
        try {
            WeatherResponse response = weatherService.getWeather(city);
            displayWeather(response);
        } catch (Exception e) {
            handleError(e);
        }
    }

    public void getAirQuality(String cityName) {
        try {
            Location location = geocodingService.getCoordinates(cityName);
            AirQualityResponse response = airQualityService.getAirQuality(location.getLat(), location.getLon());
            displayAirQuality(response);
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

    private void displayAirQuality(AirQualityResponse response) {
        System.out.println("Air Quality at (" + response.getLocation().getLatitude() + ", " + response.getLocation().getLongitude() + "):");
        System.out.println("AQI Level: " + response.getAirQuality().getAqi());
        System.out.println("Components: " + response.getAirQuality().getComponents());
        System.out.println("Health Advice: " + response.getAirQuality().getAdvice());
    }

    private void handleError(Exception e) {
        System.err.println("Error fetching data: " + e.getMessage());
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

    public void getWeatherAlerts(String city) {
        try {
            Location location = geocodingService.getCoordinates(city);
            List<WeatherAlert> alerts = weatherService.getWeatherAlerts(location.getLat(), location.getLon());
            displayWeatherAlerts(alerts);
        } catch (Exception e) {
            handleError(e);
        }
    }

    private void displayWeatherAlerts(List<WeatherAlert> alerts) {
        if (alerts.isEmpty()) {
            System.out.println("No severe weather alerts for this location.");
            return;
        }

        System.out.println("Severe Weather Alerts:");
        for (WeatherAlert alert : alerts) {
            System.out.println("Event: " + alert.getEvent());
            System.out.println("-----------");
        }
    }

    public void setDefaultCityForUser(int userId, String city) {
        try {
            userPreferencesService.setDefaultCity(userId, city);
            System.out.println("Default city set to " + city + " for user ID: " + userId);
        } catch (Exception e) {
            handleError(e);
        }
    }

    public void getWeatherForUser(int userId) {
        try {
            String defaultCity = userPreferencesService.getDefaultCity(userId);
            System.out.println("User ID: " + userId + ", Default City: " + defaultCity);
            getWeather(defaultCity);
        } catch (Exception e) {
            handleError(e);
        }
    }
}
