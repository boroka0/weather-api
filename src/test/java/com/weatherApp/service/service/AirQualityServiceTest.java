package com.weatherApp.service.service;

import com.weatherApp.model.AirQualityResponse;
import com.weatherApp.service.AirQualityService;
import com.weatherApp.util.ConfigUtil;
import com.weatherApp.util.HttpClientUtil;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;


public class AirQualityServiceTest {

    @Test
    public void testGetWeatherWithApi() {
        AirQualityService service = new AirQualityService();
        AirQualityResponse response = service.getAirQuality(30, 30);

        assertTrue(response.getAirQuality().getComponents().getO3() > -1 && response.getAirQuality().getComponents().getO3() < 300);
        assertTrue(response.getAirQuality().getComponents().getCo() > -1 && response.getAirQuality().getComponents().getCo() < 5000);
        assertTrue(response.getAirQuality().getAqi() > -1 && response.getAirQuality().getAqi() < 7);
    }

    @Test
    public void testGetAirQualityWithValidResponse() {
        AirQualityService airQualityService = new AirQualityService();

        try (MockedStatic<ConfigUtil> configUtilMock = mockStatic(ConfigUtil.class);
             MockedStatic<HttpClientUtil> httpClientUtilMock = mockStatic(HttpClientUtil.class)) {

            configUtilMock.when(() -> ConfigUtil.get("api.base.url"))
                    .thenReturn("https://api.example.com");
            configUtilMock.when(() -> ConfigUtil.get("api.key"))
                    .thenReturn("dummyApiKey");

            String mockResponseJson = """
                    {
                      "list": [
                        {
                          "main": { "aqi": 3 },
                          "components": {
                            "co": 201.94,
                            "no": 0.34,
                            "no2": 4.12,
                            "o3": 61.4,
                            "so2": 2.21,
                            "pm2_5": 18.92,
                            "pm10": 24.14,
                            "nh3": 0.8
                          },
                          "dt": 1672924800
                        }
                      ]
                    }
                    """;

            httpClientUtilMock.when(() -> HttpClientUtil.get(Mockito.anyString()))
                    .thenReturn(mockResponseJson);

            AirQualityResponse response = airQualityService.getAirQuality(40.7128, -74.0060);

            assertNotNull(response);
            assertEquals(3, response.getAirQuality().getAqi());
            assertEquals(201.94, response.getAirQuality().getComponents().getCo(), 0.01);
            assertEquals(4.12, response.getAirQuality().getComponents().getNo2(), 0.01);

            configUtilMock.verify(() -> ConfigUtil.get("api.base.url"));
            configUtilMock.verify(() -> ConfigUtil.get("api.key"));
            httpClientUtilMock.verify(() -> HttpClientUtil.get(Mockito.anyString()));
        }
    }
}

