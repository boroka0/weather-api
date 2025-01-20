package com.weatherApp.service.service;

import com.weatherApp.model.WeatherResponse;
import com.weatherApp.service.ForecastService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ForecastServiceTest {

    @Test
    public void testGetWeatherWithApi() {
        ForecastService service = new ForecastService();
        List<WeatherResponse> response = service.getForecast("Paris");

        assertEquals("Paris", response.get(0).getCityName());
        assertTrue(response.get(0).getTemperature() > -100 && response.get(0).getTemperature() < 50);
        assertTrue(response.get(0).getHumidity() >= 0 && response.get(0).getHumidity() <= 100);
    }

}