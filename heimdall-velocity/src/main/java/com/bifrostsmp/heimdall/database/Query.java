package com.bifrostsmp.heimdall.database;

import com.bifrostsmp.heimdall.HeimdallVelocity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.bifrostsmp.heimdall.HeimdallVelocity.getDataDirectory;

public class Query {

    //Main class for all the database query methods
    public static ResultSet checkPlayers(String id) throws SQLException {
        // Get database connection
        ConnectDB.connection(getDataDirectory());
        // START MySQL query
        ResultSet player;
        String checkSQL =
                "SELECT * FROM players WHERE uuid=?"; // Note the question mark as placeholders for
        // input values
        PreparedStatement checkID;
        try {
            checkID = HeimdallVelocity.connection.prepareStatement(checkSQL);
            checkID.setString(1, id); // Set first "?" to query string
            player = checkID.executeQuery();
            //      checkID.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return player;
    }

    public static void insertPlayers(String name, String id) throws SQLException {
        // Get database connection
        ConnectDB.connection(getDataDirectory());
        String insertSQL = "INSERT INTO players(name, uuid) VALUES (?,?);";
        PreparedStatement insertUser = HeimdallVelocity.connection.prepareStatement(insertSQL);
        insertUser.setString(1, name);
        insertUser.setString(2, id);
        insertUser.executeUpdate();
        insertUser.close();
    }

    public static void removePlayer(String id) {
        // Get database connection
        ConnectDB.connection(getDataDirectory());
        String deleteSQL = "DELETE FROM players WHERE uuid  = \'" + id + "\'";
        try {
            PreparedStatement removeUser = HeimdallVelocity.connection.prepareStatement(deleteSQL);
            removeUser.executeUpdate();
            removeUser.close();
        } catch (SQLException e) {
            System.out.println("Error at Query.removePlayer");
            e.printStackTrace();
        }
    }

    public static void insertServers(String[] servers) throws SQLException {
        // Get database connection
        ConnectDB.connection(getDataDirectory());
        for (String server : servers) {
            String insertSQL = "INSERT INTO servers(server) (?);";
            PreparedStatement insertServer = HeimdallVelocity.connection.prepareStatement(insertSQL);
            insertServer.setString(1, server);
            insertServer.close();
        }
    }

    public static void updateTrigger() throws SQLException {
        // Get database connection
        ConnectDB.connection(getDataDirectory());
        PreparedStatement update =
                HeimdallVelocity.connection.prepareStatement(
                        "UPDATE servers SET JSONUpdated = false WHERE JSONUpdated = true;");
        update.executeUpdate();
        update.close();
    }

    public static boolean checkForApp(long discordID) throws SQLException {
        ConnectDB.connection(getDataDirectory());
        String check = "SELECT * FROM applications WHERE discordID = " + discordID + ";";
        PreparedStatement checkForApp = HeimdallVelocity.connection.prepareStatement(check);
        return checkForApp.executeQuery().next();
    }

    public static int getAppCounter(long discordID) {
        ConnectDB.connection(getDataDirectory());
        String get = "SELECT counter FROM applications WHERE discordID = " + discordID + ";";
        try {
            PreparedStatement getAppCounter = HeimdallVelocity.connection.prepareStatement(get);
            ResultSet rs = getAppCounter.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void insertApp(long discordID, String ign, String uuid, String app, int counter) {
        ConnectDB.connection(getDataDirectory());
        String insertApp =
                "INSERT INTO applications(discordID, ign, uuid, app, counter) VALUES (?, ?, ?, ?, ?);";
        try {
            PreparedStatement insert = HeimdallVelocity.connection.prepareStatement(insertApp);
            insert.setString(1, String.valueOf(discordID));
            insert.setString(2, ign);
            insert.setString(3, uuid);
            insert.setString(4, app);
            insert.setInt(5, counter);

            insert.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void updateApp(long discordID, String app, int counter) {
        ConnectDB.connection(getDataDirectory());
        String updateApp = "UPDATE applications SET app=?, counter=? WHERE discordID=?;";
        try {
            PreparedStatement update = HeimdallVelocity.connection.prepareStatement(updateApp);
            update.setString(1, app);
            update.setInt(2, counter);
            update.setString(3, String.valueOf(discordID));
            update.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
