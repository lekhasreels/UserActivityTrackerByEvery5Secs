package com.user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class LoginApplication {
	private static final String DB_URL = "jdbc:mysql://localhost:3306/useractivedb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Sree27@532";

    private static AtomicInteger activeUserCount = new AtomicInteger(0);

    public static void main(String[] args) {
    	Timer timer = new Timer();
        timer.scheduleAtFixedRate(new RefreshTask(), 0, 60000); // Refresh every 5 seconds
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Login Application!");
        while (true) {
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Active User Count");
            System.out.println("4. Exit");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    registerUser(scanner);
                    break;
                case 2:
                    if (loginUser(scanner)) {
                        System.out.println("Login successful!");
                    } else {
                        System.out.println("Login failed. Please try again.");
                    }
                    break;
                case 3:
                    int activeUserCount = countActiveUsers();
                    System.out.println("Active User Count: " + activeUserCount);
                    break;
                case 4:
                    System.out.println("Exiting the application.");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Please choose a valid option.");
            }
        }
    }

	private static void registerUser(Scanner scanner) {
        System.out.print("Enter a username: ");
        String username = scanner.nextLine();

        System.out.print("Enter a password: ");
        String password = scanner.nextLine();

        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String insertQuery = "INSERT INTO user (username, password) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Registration successful!");
            } else {
                System.out.println("Registration failed. Please try again.");
            }
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static boolean loginUser(Scanner scanner) {
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String query = "SELECT * FROM user WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            boolean isAuthenticated = resultSet.next();

            resultSet.close();
            preparedStatement.close();
            connection.close();

            return isAuthenticated;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    private static final class RefreshTask extends TimerTask {
        @Override
        public void run() {
            int count = countActiveUsers();
            activeUserCount.set(count);
            System.out.println("Active User Count (Refreshed): " + count);
        }
    }
    private static int countActiveUsers() {
    	
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String countQuery = "SELECT COUNT(*) FROM user";
            PreparedStatement preparedStatement = connection.prepareStatement(countQuery);

            ResultSet resultSet = preparedStatement.executeQuery();

            int activeUserCount = 0;

            if (resultSet.next()) {
                activeUserCount = resultSet.getInt(1);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();

            return activeUserCount;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
     }
    
}