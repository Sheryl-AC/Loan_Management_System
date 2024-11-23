package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.IOException;

public class DBUtil {

    // Static method to establish a database connection
    public static Connection getDBConn() throws SQLException, IOException {
        String connectionString = DBPropertyUtil.getConnectionString("db.properties");
        
        // Replace "your-db-driver" with the actual JDBC driver for your database
        try {
            // Load the JDBC driver (for MySQL, for example)
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Create and return a database connection
            return DriverManager.getConnection(connectionString);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database driver not found", e);
        }
    }
}
