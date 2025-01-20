package com.weatherApp.service.service;

import com.weatherApp.model.Location;
import com.weatherApp.service.GeocodingService;
import com.weatherApp.util.ConfigUtil;
import com.weatherApp.util.HttpClientUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

public class GeocodingServiceTest {

        private GeocodingService geocodingService;

        @BeforeEach
        void setUp() {
        geocodingService = new GeocodingService();
        }
        @Test
        public void testGetCoordinatesSuccessfully() {
            try (MockedStatic<ConfigUtil> configMock = Mockito.mockStatic(ConfigUtil.class);
                 MockedStatic<HttpClientUtil> httpMock = Mockito.mockStatic(HttpClientUtil.class)) {

                configMock.when(() -> ConfigUtil.get("locationiq.api.key")).thenReturn("test-api-key");
                configMock.when(() -> ConfigUtil.get("locationiq.api.url")).thenReturn("https://test-api.url");

                String mockJsonResponse = "[{\"lat\":\"40.7128\",\"lon\":\"-74.0060\"}]";
                httpMock.when(() -> HttpClientUtil.get(anyString())).thenReturn(mockJsonResponse);

                Location location = geocodingService.getCoordinates("New York");

                assertNotNull(location);
                assertEquals(40.7128, location.getLat());
                assertEquals(-74.0060, location.getLon());
            }
        }

        @Test
        public void testGetCoordinatesConnectionFailure() {
            try (MockedStatic<ConfigUtil> configMock = Mockito.mockStatic(ConfigUtil.class);
                 MockedStatic<HttpClientUtil> httpMock = Mockito.mockStatic(HttpClientUtil.class)) {

                configMock.when(() -> ConfigUtil.get("locationiq.api.key")).thenReturn("test-api-key");
                configMock.when(() -> ConfigUtil.get("locationiq.api.url")).thenReturn("https://test-api.url");

                httpMock.when(() -> HttpClientUtil.get(anyString())).thenThrow(new IOException("Network error"));

                RuntimeException exception = assertThrows(RuntimeException.class, () -> geocodingService.getCoordinates("New York"));
                assertTrue(exception.getMessage().contains("Failed to fetch geocoding data"));
            }
        }

        @Test
        public void testGetCoordinatesInvalidJsonResponse() {
            try (MockedStatic<ConfigUtil> configMock = Mockito.mockStatic(ConfigUtil.class);
                 MockedStatic<HttpClientUtil> httpMock = Mockito.mockStatic(HttpClientUtil.class)) {

                configMock.when(() -> ConfigUtil.get("locationiq.api.key")).thenReturn("test-api-key");
                configMock.when(() -> ConfigUtil.get("locationiq.api.url")).thenReturn("https://test-api.url");

                String invalidJsonResponse = "Invalid JSON";
                httpMock.when(() -> HttpClientUtil.get(anyString())).thenReturn(invalidJsonResponse);

                IllegalStateException exception = assertThrows(IllegalStateException.class, () -> geocodingService.getCoordinates("New York"));
                assertTrue(exception.getMessage().contains("Failed to parse geocoding response JSON"));
            }
        }

        @Test
        public void testGetCoordinatesCityNotFound() {
            try (MockedStatic<ConfigUtil> configMock = Mockito.mockStatic(ConfigUtil.class);
                 MockedStatic<HttpClientUtil> httpMock = Mockito.mockStatic(HttpClientUtil.class)) {

                configMock.when(() -> ConfigUtil.get("locationiq.api.key")).thenReturn("test-api-key");
                configMock.when(() -> ConfigUtil.get("locationiq.api.url")).thenReturn("https://test-api.url");

                String emptyJsonResponse = "[]";
                httpMock.when(() -> HttpClientUtil.get(anyString())).thenReturn(emptyJsonResponse);

                IllegalStateException exception = assertThrows(IllegalStateException.class, () -> geocodingService.getCoordinates("InvalidCity"));
                assertTrue(exception.getMessage().contains("City not found"));
            }
        }
}