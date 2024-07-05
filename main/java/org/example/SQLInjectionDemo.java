package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class SQLInjectionDemo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter username:");
        String username = scanner.nextLine();

        System.out.println("Enter password:");
        String password = scanner.nextLine();

        // Attempt to authenticate user
        if (authenticateUser(username, password)) {
            System.out.println("Authentication successful!");
        } else {
            System.out.println("Authentication failed.");
        }

        scanner.close();
    }

    private static boolean authenticateUser(String username, String password) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // Connect to the database
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydatabase", "root", "password");
            statement = connection.createStatement();

            // Vulnerable SQL query
            String query = "SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password + "'";

            // Execute the query
            resultSet = statement.executeQuery(query);

            // Check if any results are returned
            return resultSet.next();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (resultSet != null) resultSet.close();
            } catch (Exception e) { /* ignored */ }
            try {
                if (statement != null) statement.close();
            } catch (Exception e) { /* ignored */ }
            try {
                if (connection != null) connection.close();
            } catch (Exception e) { /* ignored */ }
        }

        return false;
    }
}
