package com.bifrostsmp.heimdall.database;

import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB extends JavaPlugin {

    public static Connection connection; // This is the variable used to connect to the DB
    public final String serverName = getConfig().getString("server");

    public static boolean connectDB(String user, String password, String url) {
        // Init mysql db connection
        try { // try catch to get any SQL errors
            connection = DriverManager.getConnection(url, user, password);
            // with the method getConnection() from DriverManager, we're trying to set
            // the connection's url, username, password to the variables we made earlier and
            // trying to get a connection at the same time. JDBC allows us to do this.
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
