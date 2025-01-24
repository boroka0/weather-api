package com.weatherApp.repository;

import com.weatherApp.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserPreferencesRepository {
    public void setDefaultCity(int userId, String city) {
        String sql = "INSERT INTO user_preferences (user_id, default_city) VALUES (?, ?) " +
                "ON CONFLICT (user_id) DO UPDATE SET default_city = EXCLUDED.default_city";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, city);

            preparedStatement.executeUpdate();
            System.out.println("Default city updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set default city.", e);
        }
    }

    public String getDefaultCity(int userId) {
        String sql = "SELECT default_city FROM user_preferences WHERE user_id = ?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("default_city");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch default city.", e);
        }
        return null;
    }
}