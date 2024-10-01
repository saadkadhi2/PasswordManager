package com.example.passwordmanager;
import java.sql.*;
import java.sql.DriverManager;


public class DatabaseConnection {
    public Connection databaseLink;

    public Connection getConnection() {
        String dbName = "sys";
        String dbUser = "skadhi";
        String dbPassword = "Piano2017!";
        String url = "jdbc:mysql://localhost:3306/" + dbName;

        try {
            databaseLink = DriverManager.getConnection(url, dbUser, dbPassword);
            System.out.println("Database connection successful");
        } catch (Exception e) {
            System.out.println("MySQL Driver not found.");
            e.printStackTrace();
        }

        return databaseLink;
    }
}
