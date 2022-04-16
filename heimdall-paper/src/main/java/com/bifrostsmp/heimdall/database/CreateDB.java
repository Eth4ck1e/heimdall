package com.bifrostsmp.heimdall.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateDB {
  public static void createDB() {
    Connection connection = ConnectDB.connection;
    // Create SQL DB tables if not exist
    String players =
        "CREATE TABLE IF NOT EXISTS players("
            + "name char(64) NOT NULL,"
            + "uuid varchar(64) NOT NULL,"
            + "PRIMARY KEY(uuid)"
            + ");";
    // Create statement for servers table, default is set to force update on first check
    String servers =
        "CREATE TABLE IF NOT EXISTS servers(server varchar(64) NOT NULL, JSONUpdated boolean DEFAULT false, PRIMARY KEY(server));";
    try {
      PreparedStatement playersDB = connection.prepareStatement(players);
      playersDB.executeUpdate();
      PreparedStatement serversDB = connection.prepareStatement(servers);
      serversDB.executeUpdate();
    } catch (SQLException throwables) {
      // getLogger().log(INFO, ChatColor.YELLOW + "Error at CreateDB");
      throwables.printStackTrace();
    }
  }
}
