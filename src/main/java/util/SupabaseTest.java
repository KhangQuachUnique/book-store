package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class SupabaseTest {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://aws-1-ap-southeast-1.pooler.supabase.com:5432/postgres?sslmode=require&prepareThreshold=0";
        String user = "postgres.dgnmjowvneyijoioqtuk";
        String pass = "kadfwfsfsvs";
        try {
            Connection conn = DriverManager.getConnection(url, user, pass);
            System.out.println("Connection SUCCESS!");  // Nếu in ra, connection OK
            conn.close();
        } catch (Exception e) {
            System.out.println("Connection FAIL: " + e.getMessage());
            e.printStackTrace();
        }
    }
}