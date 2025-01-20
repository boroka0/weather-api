package com.weatherApp;

import com.weatherApp.controller.WeatherController;

import java.util.Scanner;

public class WeatherApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        WeatherController weatherController = new WeatherController();

        System.out.println("Welcome to the Weather App!");

        try {
            System.out.println("Options:");
            System.out.println("1. Get Weather (with weather alerts)");
            System.out.println("2. Get Forecast");
            System.out.println("3. Get Air Quality");
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
            } else {
                System.out.println("Invalid option. Exiting...");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}


