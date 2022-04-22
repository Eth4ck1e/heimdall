package com.bifrostsmp.heimdall.database;

import org.bukkit.ChatColor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.*;
import java.util.Objects;

import static java.util.logging.Level.INFO;
import static org.bukkit.Bukkit.getLogger;

public class Query {
  public static boolean insert(String server) {
    if (Objects.equals(server, "default")) {
      return false;
    } else {
      //  Insert statement to add server name from config, will be ignored if server name exists in
      // database
      String insertSQL = "INSERT IGNORE INTO servers(server) VALUE ('" + server + "');";
      //  prepare the statements to be executed
      try {
        Connection connection = ConnectDB.connection;
        PreparedStatement insertServer = connection.prepareStatement(insertSQL);
        insertServer.executeUpdate();
        return true;
        // use executeUpdate() to update the database table
      } catch (SQLException e) {
        return false;
      }
    }
  }

  public static int insertPlayers(String name, String id) {
    String insertSQL = "INSERT IGNORE INTO players(name, uuid) VALUES (?,?);";
    try {
      Connection connection = ConnectDB.connection;
      PreparedStatement insertUser = connection.prepareStatement(insertSQL);
      insertUser.setString(1, name);
      insertUser.setString(2, id);
      return insertUser.executeUpdate();
    } catch (SQLException e) {
      getLogger().log(INFO, ChatColor.YELLOW + "Error at Query.insertPlayers");
      e.printStackTrace();
    }
    return 0;
  }

  public static int removePlayers(String id) {
    String deleteSQL = "DELETE FROM players WHERE uuid = \'" + id + "\';";
    try {
      Connection connection = ConnectDB.connection;
      PreparedStatement removeUser = connection.prepareStatement(deleteSQL);
      return removeUser.executeUpdate();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return 0;
  }

  public static boolean checkPlayer(String id) {
    String checkSQL = "SELECT * FROM players WHERE uuid=\'" + id + "\';";
    boolean ResultSet;
    try {
      Connection connection = ConnectDB.connection;
      PreparedStatement checkPlayer = connection.prepareStatement(checkSQL);
      ResultSet = checkPlayer.execute();
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
    return ResultSet;
  }

  public static boolean check(String server) {
    boolean value = false;
    String query = "SELECT JSONUpdated FROM servers WHERE server = \'" + server + "\';";
    try (Statement stmt = ConnectDB.connection.createStatement()) {
      ResultSet result = stmt.executeQuery(query);
      result.next();
      value = result.getObject(1, Boolean.class);
      // getLogger().log(INFO, ChatColor.YELLOW + "[Heimdall] current JSONUpdated value from check:
      // " + value);
    } catch (SQLException throwables) {
      // getLogger().log(INFO, ChatColor.YELLOW + "Error at Query.check");
      throwables.printStackTrace();
    }
    return value;
  }

  public static void update() {
    String query = "UPDATE servers SET JSONUpdated = false WHERE JSONUpdated = true;";
    try {
      PreparedStatement preparedStatement = ConnectDB.connection.prepareStatement(query);
      preparedStatement.executeUpdate();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public static void updated(String server) {
    String query = "UPDATE servers SET JSONUpdated = true WHERE server=?;";
    try {
      PreparedStatement preparedStatement = ConnectDB.connection.prepareStatement(query);
      preparedStatement.setString(1, server);
      preparedStatement.executeUpdate();
    } catch (SQLException throwables) {
      // getLogger().log(INFO, ChatColor.YELLOW + "Error at Query.updated");
      throwables.printStackTrace();
    }
  }

  public static JSONArray dump() {
    ResultSet result;
    String query = "SELECT name, uuid FROM players;";
    JSONObject obj = new JSONObject();
    JSONArray array = new JSONArray();
    try (Statement stmt = ConnectDB.connection.createStatement()) {
      array = new JSONArray();
      result = stmt.executeQuery(query);
      while (result.next()) {
        obj = new JSONObject();
        obj.put("uuid", result.getString("uuid"));
        obj.put("name", result.getString("name"));
        array.add(obj);
      }
    } catch (SQLException throwables) {
      // getLogger().log(INFO, ChatColor.YELLOW + "Error at Query.dump");
      throwables.printStackTrace();
    }
    return array;
  }
}
