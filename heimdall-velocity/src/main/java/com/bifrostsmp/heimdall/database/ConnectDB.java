package com.bifrostsmp.heimdall.database;

import com.bifrostsmp.heimdall.HeimdallVelocity;
import com.bifrostsmp.heimdall.config.ConfigParser;

import java.nio.file.Path;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {

    public static void connection(Path dataDirectory) {
        // required to load jdbc driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        ConfigParser.parse(dataDirectory);
        String url = ConfigParser.getUrl();
        String user = ConfigParser.getUser();
        String password = ConfigParser.getPassword();

        // Init mysql db connection
        try { // try catch to get any SQL errors
            HeimdallVelocity.connection =
                    DriverManager.getConnection(url + "?autoReconnect=true", user, password);
            HeimdallVelocity.logger.info("mySQL database connection successful!");
            // with the method getConnection() from DriverManager, we're trying to set
            // the connection's url, username, password to the variables we made earlier and
            // trying to get a connection at the same time. JDBC allows us to do this.
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
