package com.weatherApp;

import com.weatherApp.controller.WeatherController;
import com.weatherApp.model.Weather;
import com.weatherApp.model.WeatherResponse;
import com.weatherApp.service.ForecastService;
import com.weatherApp.service.WeatherService;

import java.util.List;
import java.util.Scanner;

public class WeatherApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        WeatherService weatherService = new WeatherService();
        ForecastService forecastService = new ForecastService();

        System.out.println("Welcome to the Weather App!");

        try {
            System.out.print("Enter the city name: ");
            String city = scanner.nextLine();

            System.out.print("Enter the number of days for the forecast (1-5): ");
            int days = scanner.nextInt();

            if (days < 1 || days > 5) {
                System.out.println("Please enter a number between 1 and 5 for the forecast duration.");
                return;
            }

            // Fetch forecast data
            List<WeatherResponse> forecast = forecastService.getForecast(city);

            // Display forecast data
            System.out.println("\nWeather Forecast for " + city + ":");
            for (WeatherResponse response : forecast) {
                System.out.println("---------------------------");
                System.out.println("Date: " + response.getDate()); // Assuming your response has a date field
                System.out.println("Temperature: " + response.getTemperature() + "°C");
                System.out.println("Humidity: " + response.getHumidity() + "%");
                System.out.println("Pressure: " + response.getPressure() + " hPa");
                System.out.println("Description: " + response.getWeatherDescription());
                System.out.println("Sunrise: " + response.getSunrise());
                System.out.println("Sunrise: " + response.getSunset());
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
