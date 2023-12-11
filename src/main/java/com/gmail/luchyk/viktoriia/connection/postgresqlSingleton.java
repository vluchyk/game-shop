package com.gmail.luchyk.viktoriia.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class postgresqlSingleton {
    private static Connection connection;
    private static final String name = "postgres";
    private static final String password = "admin";

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5435/hillel", name, password);
        }
        return connection;
    }
}