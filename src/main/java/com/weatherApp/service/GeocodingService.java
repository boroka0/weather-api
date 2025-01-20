package com.weatherApp.service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.weatherApp.model.GeocodingResponse;
import com.weatherApp.model.Location;
import com.weatherApp.util.ConfigUtil;
import com.weatherApp.util.HttpClientUtil;

import java.io.IOException;

public class GeocodingService {
    public Location getCoordinates(String cityName) {
        String apiKey = ConfigUtil.get("locationiq.api.key");
        String baseUrl = ConfigUtil.get("locationiq.api.url");

        String url = String.format("%s?key=%s&q=%s&format=json",
                baseUrl,
                apiKey,
                cityName.replace(" ", "+"));

        String responseJson;

        try {
            responseJson = HttpClientUtil.get(url);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch geocoding data: Unable to connect to the API.", e);
        }

        GeocodingResponse[] geocodingResponses;
        try {
            geocodingResponses = new Gson().fromJson(responseJson, GeocodingResponse[].class);
        } catch (JsonSyntaxException e) {
            throw new IllegalStateException("Failed to parse geocoding response JSON. Raw response: " + responseJson, e);
        }

        if (geocodingResponses == null || geocodingResponses.length == 0) {
            throw new IllegalStateException("City not found: " + cityName + ". Please check the city name.");
        }

        GeocodingResponse firstResult = geocodingResponses[0];
        return new Location(Double.parseDouble(firstResult.getLat()), Double.parseDouble(firstResult.getLon()));
    }
}