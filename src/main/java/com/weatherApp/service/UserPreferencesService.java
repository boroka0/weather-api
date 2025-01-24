package com.weatherApp.service;

import com.weatherApp.repository.UserPreferencesRepository;

public class UserPreferencesService {
    private final UserPreferencesRepository repository;

    public UserPreferencesService() {
        this.repository = new UserPreferencesRepository();
    }

    public void setDefaultCity(int userId, String city) {
        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("City name cannot be null or empty.");
        }

        repository.setDefaultCity(userId, city);
    }

    public String getDefaultCity(int userId) {
        String city = repository.getDefaultCity(userId);
        if (city == null) {
            throw new IllegalStateException("No default city found for user ID: " + userId);
        }
        return city.trim();
    }
}

