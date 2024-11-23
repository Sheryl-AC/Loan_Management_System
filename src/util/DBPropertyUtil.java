package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DBPropertyUtil {
    // Method to get the database connection string
    public static String getConnectionString(String fileName) throws IOException {
        Properties properties = new Properties();
        // Load the properties file
        FileInputStream inputStream = new FileInputStream(fileName);
        properties.load(inputStream);
        inputStream.close();
        
        // Return the connection string from the properties
        return properties.getProperty("db.url");
    }
}

