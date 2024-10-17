
package com.example.employee;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import com.example.employee.Employee;

public class RemoteSQLServerConnection {
    public static void main(String[] args) {
        // Replace the following variables with your own values
        String serverName = "10.1.1.191";    // Replace with remote server IP or hostname
        String portNumber = "1434";          // Ensure this is the correct port
        String databaseName = "MyDatabase";  // Replace with your database name
        String username = "sa";              // SQL Server username
        String password = "password";        // SQL Server password

        // Correct JDBC URL format
        String connectionUrl = "jdbc:sqlserver://" + serverName + ":" + portNumber + ";databaseName=" + databaseName + ";user=" + username + ";password=" + password + ";encrypt=true;trustServerCertificate=true;";

        Connection connection = null;

        try {
            
            // Establish the connection
            connection = DriverManager.getConnection(connectionUrl);
            System.out.println("Connection to the remote SQL Server established successfully!");
            System.out.println(connectionUrl);
            
            // Perform your database operations here
            

        } catch (SQLException e) {
            System.out.printf(e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("Connection closed.");
                } catch (SQLException e) {
                    System.out.printf(e.getMessage());
                }
            }
        }
    }
}
