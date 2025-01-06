package com.weatherApp.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

public class HttpClientUtil {
    public static String get(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                return reader.lines().collect(Collectors.joining());
            }
        } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
            throw new IOException("City not found. Please check the city name.");
        } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
            throw new IOException("Invalid API key");
        } else {
            throw new IOException("Unexpected HTTP error: " + responseCode);
        }
    }

}
