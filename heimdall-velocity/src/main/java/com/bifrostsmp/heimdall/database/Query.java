package com.bifrostsmp.heimdall.database;

import com.bifrostsmp.heimdall.HeimdallVelocity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.bifrostsmp.heimdall.HeimdallVelocity.getDataDirectory;

public class Query {

  public static ResultSet checkPlayers(String id) throws SQLException {
    //Get database connection
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
    //Get database connection
    ConnectDB.connection(getDataDirectory());
    String insertSQL = "INSERT INTO players(name, uuid) VALUES (?,?);";
    PreparedStatement insertUser = HeimdallVelocity.connection.prepareStatement(insertSQL);
    insertUser.setString(1, name);
    insertUser.setString(2, id);
    insertUser.executeUpdate();
    insertUser.close();
  }

  public static void removePlayer(String id) {
    //Get database connection
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
    //Get database connection
    ConnectDB.connection(getDataDirectory());
    for (String server : servers) {
      String insertSQL = "INSERT INTO servers(server) (?);";
      PreparedStatement insertServer = HeimdallVelocity.connection.prepareStatement(insertSQL);
      insertServer.setString(1, server);
      insertServer.close();
    }
  }

  public static void updateTrigger() throws SQLException {
    //Get database connection
    ConnectDB.connection(getDataDirectory());
    PreparedStatement update =
        HeimdallVelocity.connection.prepareStatement(
            "UPDATE servers SET JSONUpdated = false WHERE JSONUpdated = true;");
    update.executeUpdate();
    update.close();
  }
}
