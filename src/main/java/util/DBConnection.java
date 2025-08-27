package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    static String url = "jdbc:postgresql://db.tqlsarbgxbmhohiyykaf.supabase.co:5432/postgres?sslmode=require";
    static String user = "postgres";
    static String password = "1101";

    static {
        try {
            Class.forName("org.postgresql.Driver"); // Load driver
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
