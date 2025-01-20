package com.weatherApp.service.util;

import com.weatherApp.util.ConfigUtil;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;

public class ConfigUtilTest {

    @Test
    public void testGetWeather_Success() throws Exception {
        try (MockedStatic<ConfigUtil> configUtilMock = mockStatic(ConfigUtil.class)) {

            configUtilMock.when(() -> ConfigUtil.get("api.base.url"))
                    .thenReturn("https://api.example.com");
            configUtilMock.when(() -> ConfigUtil.get("api.key"))
                    .thenReturn("dummyApiKey");

            String baseUrl = ConfigUtil.get("api.base.url");
            String apiKey = ConfigUtil.get("api.key");

            assertEquals("https://api.example.com", baseUrl);
            assertEquals("dummyApiKey", apiKey);

            configUtilMock.verify(() -> ConfigUtil.get("api.base.url"));
            configUtilMock.verify(() -> ConfigUtil.get("api.key"));
        }
    }
}