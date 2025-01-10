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
        String url = String.format("%s/geo/1.0/direct?q=%s&limit=1&appid=%s",
                ConfigUtil.get("api.base.url"),
                cityName,
                ConfigUtil.get("api.key"));

        String responseJson;

        try {
            responseJson = HttpClientUtil.get(url);
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch geocoding data.", e);
        }

        GeocodingResponse[] geocodingResponses;
        try {
            geocodingResponses = new Gson().fromJson(responseJson, GeocodingResponse[].class);
        } catch (JsonSyntaxException e) {
            throw new IllegalStateException("Failed to parse geocoding response JSON.", e);
        }

        if (geocodingResponses == null || geocodingResponses.length == 0) {
            throw new IllegalStateException("No geocoding results found for the city: " + cityName);
        }

        GeocodingResponse firstResult = geocodingResponses[0];
        return new Location(firstResult.getLat(), firstResult.getLon());
    }
}
