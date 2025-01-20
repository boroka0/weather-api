package main.test.service;

import com.weatherApp.model.WeatherResponse;
import com.weatherApp.service.WeatherService;
import com.weatherApp.util.ConfigUtil;
import com.weatherApp.util.HttpClientUtil;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;

public class WeatherServiceTest {

    @Test
    public void testGetWeatherWithApi() throws Exception {
        WeatherService service = new WeatherService();
        WeatherResponse response = service.getWeather("Paris");

        assertEquals("Paris", response.getCityName());
        assertTrue(response.getTemperature() > -100 && response.getTemperature() < 50);
        assertTrue(response.getHumidity() >= 0 && response.getHumidity() <= 100);
    }

    @Test
    public void testGetWeatherWithSuccessfulResponse() throws Exception {
        WeatherService weatherService = new WeatherService();

        try (MockedStatic<ConfigUtil> configUtilMock = mockStatic(ConfigUtil.class);
             MockedStatic<HttpClientUtil> httpClientUtilMock = mockStatic(HttpClientUtil.class)) {

            configUtilMock.when(() -> ConfigUtil.get("api.base.url"))
                    .thenReturn("https://api.example.com");
            configUtilMock.when(() -> ConfigUtil.get("api.key"))
                    .thenReturn("dummyApiKey");

            String mockCity = "London";
            String mockResponseJson = "{"
                    + "\"name\":\"London\","
                    + "\"main\":{"
                    + "\"temp\":15.0,"
                    + "\"humidity\":60,"
                    + "\"pressure\":1015"
                    + "},"
                    + "\"weather\":[{\"description\":\"clear sky\"}]"
                    + "}";

            httpClientUtilMock.when(() -> HttpClientUtil.get(Mockito.anyString()))
                    .thenReturn(mockResponseJson);

            WeatherResponse response = weatherService.getWeather(mockCity);

            assertNotNull(response);
            assertEquals("London", response.getCityName());
            assertEquals(15.0, response.getTemperature());
            assertEquals(60, response.getHumidity());
            assertEquals(1015, response.getPressure());
            assertEquals("clear sky", response.getWeatherDescription());

            String expectedUrl = "https://api.example.com/weather?q=London&units=metric&appid=dummyApiKey";
            ((MockedStatic<?>) httpClientUtilMock).verify(() -> HttpClientUtil.get(expectedUrl));
        }
    }

    @Test
    public void testGetWeatherWithInvalidResponse() throws Exception {
        WeatherService weatherService = new WeatherService();

        try (MockedStatic<ConfigUtil> configUtilMock = mockStatic(ConfigUtil.class);
             MockedStatic<HttpClientUtil> httpClientUtilMock = mockStatic(HttpClientUtil.class)) {

            configUtilMock.when(() -> ConfigUtil.get("api.base.url"))
                    .thenReturn("https://api.example.com");
            configUtilMock.when(() -> ConfigUtil.get("api.key"))
                    .thenReturn("dummyApiKey");

            String mockCity = "InvalidCity";
            String mockResponseJson = "{}";

            httpClientUtilMock.when(() -> HttpClientUtil.get(Mockito.anyString()))
                    .thenReturn(mockResponseJson);

            Exception exception = assertThrows(IllegalStateException.class, () ->
            {
                weatherService.getWeather(mockCity);
            });

            assertEquals("Invalid response from weather API.", exception.getMessage());
        }
    }
}
