package com.weatherApp;

import com.weatherApp.controller.WeatherController;

import java.util.Scanner;

public class WeatherApp {

    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {
            WeatherController weatherController = new WeatherController();
            System.out.println("Welcome to the Weather App!");
            System.out.println("Options:");
            System.out.println("1. Get Weather (with weather alerts)");
            System.out.println("2. Get Forecast");
            System.out.println("3. Get Air Quality");
            System.out.println("4. Get Weather for Default Location");
            System.out.print("Choose an option by entering a number: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            if (option == 1) {
                System.out.print("Enter the city name: ");
                String city = scanner.nextLine();
                weatherController.getWeather(city);
                weatherController.getWeatherAlerts(city);
            } else if (option == 2) {
                System.out.print("Enter the city name: ");
                String city = scanner.nextLine();
                weatherController.getForecast(city);
            } else if (option == 3) {
                System.out.print("Enter the city name: ");
                String city = scanner.nextLine();
                weatherController.getAirQuality(city);
            } else if (option == 4) {
                System.out.print("Enter your user id: ");
                int userId = scanner.nextInt();
                weatherController.getWeatherForUser(userId);
            } else {
                System.out.println("Invalid option. Exiting...");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}


