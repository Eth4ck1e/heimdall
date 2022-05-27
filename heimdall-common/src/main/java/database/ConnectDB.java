package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
    public static Connection connection;

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
