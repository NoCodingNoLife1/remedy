package com.mlms.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtil {
    private static final String URL = "jdbc:sqlite:mlms.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Create patients table
            stmt.execute("CREATE TABLE IF NOT EXISTS patients (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "patient_number TEXT," +
                    "name TEXT NOT NULL," +
                    "age INTEGER," + // Kept for backwards compatibility
                    "dob TEXT," +
                    "gender TEXT," +
                    "phone TEXT," +
                    "address TEXT," +
                    "registered_date TEXT" +
                    ")");

            // Migration: Add new columns if they don't exist
            try {
                stmt.execute("ALTER TABLE patients ADD COLUMN patient_number TEXT");
            } catch (SQLException e) { /* Column already exists */ }
            try {
                stmt.execute("ALTER TABLE patients ADD COLUMN dob TEXT");
            } catch (SQLException e) { /* Column already exists */ }
            try {
                stmt.execute("ALTER TABLE patients ADD COLUMN address TEXT");
            } catch (SQLException e) { /* Column already exists */ }

            // Drop old tables to migrate to new schema
            stmt.execute("DROP TABLE IF EXISTS order_results");

            // Create tests table (New Schema)
            stmt.execute("CREATE TABLE IF NOT EXISTS tests (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "test_number TEXT UNIQUE NOT NULL," +
                    "test_name TEXT NOT NULL," +
                    "test_cost REAL" +
                    ")");

            // Drop old orders and order_results tables for overhaul
            stmt.execute("DROP TABLE IF EXISTS order_results");
            stmt.execute("DROP TABLE IF EXISTS orders");

            // Create results table (New Schema)
            stmt.execute("CREATE TABLE IF NOT EXISTS results (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "result_number TEXT UNIQUE NOT NULL," +
                    "patient_id INTEGER," +
                    "test_number TEXT," +
                    "test_result TEXT," +
                    "result_date TEXT," +
                    "test_cost REAL," +
                    "FOREIGN KEY (patient_id) REFERENCES patients(id)," +
                    "FOREIGN KEY (test_number) REFERENCES tests(test_number)" +
                    ")");

            // Create settings table for app configuration like password
            stmt.execute("CREATE TABLE IF NOT EXISTS settings (" +
                    "key TEXT PRIMARY KEY," +
                    "value TEXT" +
                    ")");

            // Insert default password if not exists
            try {
                stmt.execute("INSERT OR IGNORE INTO settings (key, value) VALUES ('app_password', 'password@123')");
            } catch (SQLException e) {
                // Ignore if it fails for some reason
            }

        } catch (SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
        }
    }

    public static String getAppPassword() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             java.sql.ResultSet rs = stmt.executeQuery("SELECT value FROM settings WHERE key = 'app_password'")) {
            if (rs.next()) {
                return rs.getString("value");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "password@123";
    }

    public static void updateAppPassword(String newPassword) {
        try (Connection conn = getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement("UPDATE settings SET value = ? WHERE key = 'app_password'")) {
            pstmt.setString(1, newPassword);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
