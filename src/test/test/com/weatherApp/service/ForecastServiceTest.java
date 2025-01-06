package com.weatherApp.service;

import com.weatherApp.model.WeatherResponse;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ForecastServiceTest {

    @Test
    public void testGetWeatherWithApi() throws Exception {
        ForecastService service = new ForecastService();
        List<WeatherResponse> response = service.getForecast("Paris");

        assertEquals("Paris", response.get(0).getCityName());
        assertTrue(response.get(0).getTemperature() > -100 && response.get(0).getTemperature() < 50);
        assertTrue(response.get(0).getHumidity() >= 0 && response.get(0).getHumidity() <= 100);
    }

}