package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
    private static Connection connection;
    private static String user;
    private static String password;
    private static String url;


    public static boolean connectDB(String user, String password, String url) {
        // Init mysql db connection
        try { // try catch to get any SQL errors
            connection = DriverManager.getConnection(url, user, password);
            // with the method getConnection() from DriverManager, we're trying to set
            // the connection's url, username, password to the variables we made earlier and
            // trying to get a connection at the same time. JDBC allows us to do this.
            setUser(user);
            setPassword(password);
            setUrl(url);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(getUrl(), getUser(), getPassword());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getUser() {
        return user;
    }

    public static String getPassword() {
        return password;
    }

    public static String getUrl() {
        return url;
    }

    public static void setUser(String user) {
        ConnectDB.user = user;
    }

    public static void setPassword(String password) {
        ConnectDB.password = password;
    }

    public static void setUrl(String url) {
        ConnectDB.url = url;
    }
}
