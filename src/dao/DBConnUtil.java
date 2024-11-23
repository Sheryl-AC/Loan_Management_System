package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnUtil {

    // Static method to establish a connection to the database
    public static Connection getDBConn() throws SQLException {
        // Database connection details
        String url = "jdbc:mysql://localhost:3306/loan_management_system";
        String username = "root"; 
        String password = "mahimouly@7"; 

        try {
            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establish and return a connection to the database
            return DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            // Handle errors related to JDBC Driver not found
            e.printStackTrace();
            throw new SQLException("Database connection failed: JDBC Driver not found");
        } catch (SQLException e) {
            // Handle SQL errors
            e.printStackTrace();
            throw new SQLException("Database connection failed: " + e.getMessage());
        }
    }
}
