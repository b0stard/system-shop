package org.hgwards;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public static Connection getConnection(String url,String user,String password) throws SQLException {

        return DriverManager.getConnection(url, user, password);

    }
}
