package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateDB {
    public static void create() {
        Connection connection = ConnectDB.connection;

        // Create SQL DB tables if not exist
        String players =
                "CREATE TABLE IF NOT EXISTS players("
                        + "name char(64) NOT NULL,"
                        + "uuid varchar(64) NOT NULL,"
                        + "PRIMARY KEY(uuid)"
                        + ");";
        String servers =
                "CREATE TABLE IF NOT EXISTS servers(server varchar(64) NOT NULL, JSONUpdated boolean DEFAULT true, PRIMARY KEY(server));";
        String applications =
                "CREATE TABLE IF NOT EXISTS applications ("
                        + "discordID varchar(64) NOT NULL, "
                        + "ign varchar(64) NOT NULL, "
                        + "uuid varchar(64) NOT NULL, "
                        + "app JSON NOT NULL, "
                        + "counter integer NOT NULL DEFAULT 1, "
                        + "PRIMARY KEY (discordID)"
                        + ");";

        //  prepare the statements to be executed
        try {
            PreparedStatement playersDB = connection.prepareStatement(players);
            playersDB.executeUpdate();
            PreparedStatement serversDB = connection.prepareStatement(servers);
            serversDB.executeUpdate();
            PreparedStatement appDB = connection.prepareStatement(applications);
            appDB.executeUpdate();
            // use executeUpdate() to update the database table
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
