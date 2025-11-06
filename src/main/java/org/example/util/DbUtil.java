package org.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {
    public static Connection getConnection() throws SQLException {
        String url = Env.get("MYSQL_URL");
        String user = Env.get("MYSQL_USER");
        String pass = Env.get("MYSQL_PASS");
        return DriverManager.getConnection(url, user, pass);
    }
}
