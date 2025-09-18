package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database connection utility class for managing PostgreSQL connections.
 * Provides centralized database connection management for the BookieCake application.
 * 
 * @author BookieCake Team
 * @version 1.0
 */
public class DBConnection {
    
    // Database configuration constants
    private static final String DB_URL = "jdbc:postgresql://aws-1-us-east-2.pooler.supabase.com:5432/postgres?user=postgres.tqlsarbgxbmhohiyykaf&password=1101";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "1101";
    private static final String DB_DRIVER = "org.postgresql.Driver";
    
    // Static block to load the PostgreSQL JDBC driver
    static {
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load PostgreSQL JDBC driver", e);
        }
    }
    
    /**
     * Establishes and returns a database connection.
     * 
     * @return a Connection object to the PostgreSQL database
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
    
    /**
     * Closes a database connection safely.
     * This method handles null connections gracefully.
     * 
     * @param connection the connection to close, can be null
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                // Log the error but don't throw to avoid masking original exceptions
                System.err.println("Warning: Failed to close database connection: " + e.getMessage());
            }
        }
    }
    
    /**
     * Tests the database connection.
     * 
     * @return true if connection is successful, false otherwise
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
