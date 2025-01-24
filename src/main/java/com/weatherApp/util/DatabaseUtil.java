package com.weatherApp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    private static final String URL = ConfigUtil.get("db.url");
    private static final String USERNAME = ConfigUtil.get("db.username");
    private static final String PASSWORD = ConfigUtil.get("db.password");

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}