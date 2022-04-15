package com.bifrostsmp.heimdall.database;

import com.bifrostsmp.heimdall.HeimdallVelocity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Query {
  private static PreparedStatement checkID;

  public static ResultSet checkPlayers(String id) throws SQLException {
    // START MySQL query
    String checkSQL =
        "SELECT * FROM players WHERE uuid=?"; // Note the question mark as placeholders for
    // input values
    try {
      checkID = HeimdallVelocity.connection.prepareStatement(checkSQL);
      checkID.setString(1, id); // Set first "?" to query string
      return checkID.executeQuery();
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static void insertPlayers(String name, String id) throws SQLException {
    String insertSQL = "INSERT INTO players(name, uuid) VALUES (?,?);";
    PreparedStatement insertUser = HeimdallVelocity.connection.prepareStatement(insertSQL);
    insertUser.setString(1, name);
    insertUser.setString(2, id);
    insertUser.executeUpdate();
  }

  public static void insertServers(String[] servers) throws SQLException {
    for (int i = 0; i < servers.length; i++) {
      String insertSQL = "INSERT INTO servers(server) (?);";
      PreparedStatement insertServer = HeimdallVelocity.connection.prepareStatement(insertSQL);
      insertServer.setString(1, servers[i]);
    }
  }

  public static void updateTrigger() throws SQLException {
    PreparedStatement update =
        HeimdallVelocity.connection.prepareStatement(
            "UPDATE servers SET JSONUpdated = false WHERE JSONUpdated = true;");
    update.executeUpdate();
  }
}
