package com.bifrostsmp.heimdall.database;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.*;
import java.util.Objects;

public class Query {
    public static boolean insert(String server) {
        if (Objects.equals(server, "default")) {
            return false;
        } else {
            //  Insert statement to add server name from config, will be ignored if server name exists in database
            String insertSQL = "INSERT IGNORE INTO servers(server) VALUE ('" +server+ "');";
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

    public static void insertPlayers(String name, String id) throws SQLException {
        String insertSQL = "INSERT INTO players(name, uuid) VALUES (?,?);";
        PreparedStatement insertUser = ConnectDB.connection.prepareStatement(insertSQL);
        insertUser.setString(1, name);
        insertUser.setString(2, id);
        insertUser.executeUpdate();
    }

    public static boolean check(String server) {
        boolean value = false;
        String query = "SELECT JSONUpdated FROM servers WHERE server = \'" + server + "\';";
        try (Statement stmt = ConnectDB.connection.createStatement()) {
            ResultSet result = stmt.executeQuery(query);
            result.next();
            value = result.getObject(1,Boolean.class);
            //getLogger().log(INFO, ChatColor.YELLOW + "[Heimdall] current JSONUpdated value from check: " + value);
        } catch (SQLException throwables) {
            //getLogger().log(INFO, ChatColor.YELLOW + "Error at Query.check");
            throwables.printStackTrace();
        }
        return value;
    }

    public static void updated(String server) {
        String query = "UPDATE servers SET JSONUpdated = true WHERE server=?;";
        try {
            PreparedStatement preparedStatement = ConnectDB.connection.prepareStatement(query);
            preparedStatement.setString(1,server);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            //getLogger().log(INFO, ChatColor.YELLOW + "Error at Query.updated");
            throwables.printStackTrace();
        }
    }

    public static JSONArray dump() {
        ResultSet result;
        String query =
        "SELECT name, uuid FROM players;";
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
            //getLogger().log(INFO, ChatColor.YELLOW + "Error at Query.dump");
            throwables.printStackTrace();
        }
        return array;
    }
}
