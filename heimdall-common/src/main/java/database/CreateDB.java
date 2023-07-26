package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateDB {
    public static void create(String user, String password, String url) {

        // Create SQL DB tables if not exist
        String players =
                "CREATE TABLE IF NOT EXISTS players("
                        + "name char(64) NOT NULL,"
                        + "uuid varchar(64) NOT NULL,"
                        + "whitelisted boolean default false,"
                        + "discordID varchar(64),"
                        + "PRIMARY KEY(uuid)"
                        + ");";
        String servers =
                "CREATE TABLE IF NOT EXISTS servers(server varchar(64) NOT NULL, JSONUpdated boolean DEFAULT true, PRIMARY KEY(server));";

        //  prepare the statements to be executed
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement playersDB = connection.prepareStatement(players);
            playersDB.executeUpdate();
            PreparedStatement serversDB = connection.prepareStatement(servers);
            serversDB.executeUpdate();
            // use executeUpdate() to update the database table
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
