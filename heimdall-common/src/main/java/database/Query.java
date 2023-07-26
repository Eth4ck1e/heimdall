package database;

import org.bukkit.ChatColor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.*;
import java.util.List;
import java.util.Objects;

import static java.util.logging.Level.INFO;
import static org.bukkit.Bukkit.getLogger;

public class Query {

    public static int updateWhitelistByDiscordID(String id, boolean bool) {
        String updateSQL = "UPDATE players SET whitelisted=? WHERE discordID=?;";
        try (Connection connection = ConnectDB.getConnection()) {
            PreparedStatement updateStatement = connection.prepareStatement(updateSQL);
            updateStatement.setBoolean(1, bool);
            updateStatement.setString(2, id);
            return updateStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
//            getLogger().log(INFO, ChatColor.YELLOW + "Error at Query.insertWhitelisted");
        }
        return 0;
    }

    public static int updateWhitelist(String id, boolean bool) {
        String updateSQL = "UPDATE players SET whitelisted=? WHERE uuid=?;";
        try (Connection connection = ConnectDB.getConnection()) {
            PreparedStatement updateStatement = connection.prepareStatement(updateSQL);
            updateStatement.setBoolean(1, bool);
            updateStatement.setString(2, id);
            return updateStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
//            getLogger().log(INFO, ChatColor.YELLOW + "Error at Query.insertWhitelisted");
        }
        return 0;
    }

    public static void whitelistImportBatchInsert(List<String> names, List<String> uuids, List<Boolean> bools) {
        String whitelistRecovery = "INSERT IGNORE INTO players(name, uuid, whitelisted) VALUES (?,?,?);";
        try (Connection connection = ConnectDB.getConnection()) {
            PreparedStatement whitelistRecover = connection.prepareStatement(whitelistRecovery);
            for (int i = 0; i < names.size(); i++) {
                whitelistRecover.setString(1, names.get(i));
                whitelistRecover.setString(2,uuids.get(i));
                whitelistRecover.setBoolean(3, bools.get(i));
                whitelistRecover.addBatch();

                //Execute the batch after every 100 inserts
                if ((i + 1) % 100 == 0 || i == names.size() - 1) {
                    whitelistRecover.executeBatch();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int whitelistImportInsert(String name, String uuid, boolean bool) {
        String whitelistRecovery = "INSERT IGNORE INTO players(name, uuid, whitelisted) VALUES (?,?,?);";
        try (Connection connection = ConnectDB.getConnection()) {
            PreparedStatement whitelistRecover = connection.prepareStatement(whitelistRecovery);
            whitelistRecover.setString(1, name);
            whitelistRecover.setString(2, uuid);
            whitelistRecover.setBoolean(3, bool);
            return whitelistRecover.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
//            getLogger().log(INFO, ChatColor.YELLOW + "Error at Query.insertWhitelisted");
        }
        return 0;
    }
    public static boolean insertServer(String server) {
        if (Objects.equals(server, "default")) {
            return false;
        } else {
            //  Insert statement to add server name from config, will be ignored if server name exists in
            // database
            String insertSQL = "INSERT IGNORE INTO servers(server) VALUE (?);";
            //  prepare the statements to be executed
            try (Connection connection = ConnectDB.getConnection()) {
                PreparedStatement insertServer = connection.prepareStatement(insertSQL);
                insertServer.setString(1, server);
                insertServer.executeUpdate();
                return true;
                // use executeUpdate() to update the database table
            } catch (SQLException e) {
                return false;
            }
        }
    }

    public static int insertPlayer(String name, String id, String discordID) {
        String insertOrUpdateSQL = "INSERT INTO players(name, uuid, discordID) VALUES (?,?,?) " +
                "ON DUPLICATE KEY UPDATE name=VALUES(name), discordID=VALUES(discordID);";
        try (Connection connection = ConnectDB.getConnection()) {
            PreparedStatement insertUser = connection.prepareStatement(insertOrUpdateSQL);
            insertUser.setString(1, name);
            insertUser.setString(2, id);
            insertUser.setString(3, discordID);
            return insertUser.executeUpdate();
        } catch (SQLException e) {
            getLogger().log(INFO, ChatColor.YELLOW + "Error at Query.insertPlayers");
            e.printStackTrace();
        }
        return 0;
    }

    public static int removePlayer(String id) {
        String deleteSQL = "DELETE FROM players WHERE uuid = ?;";
        try (Connection connection = ConnectDB.getConnection()) {
            PreparedStatement removeUser = connection.prepareStatement(deleteSQL);
            removeUser.setString(1, id);
            return removeUser.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean checkPlayer(String id) {
        String checkSQL = "SELECT EXISTS(SELECT * FROM players WHERE uuid=?);";
        try (Connection connection = ConnectDB.getConnection()) {
            PreparedStatement checkPlayer = connection.prepareStatement(checkSQL);
            checkPlayer.setString(1, id);
            ResultSet result = checkPlayer.executeQuery();
            if (result.next()) {
                return result.getBoolean(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean checkWhitelisted(String id) {
        boolean value = false;
        String query = "SELECT whitelisted FROM players WHERE uuid=?;";
        try (Connection connection = ConnectDB.getConnection()) {
            PreparedStatement checkWhitelisted = connection.prepareStatement(query);
            checkWhitelisted.setString(1, id);
            ResultSet result = checkWhitelisted.executeQuery();

            // Check if the result set has any data
            if (result.next()) {
                // Retrieve the whitelisted value from the result set
                value = result.getBoolean("whitelisted");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static JSONArray dump() {
        ResultSet result;
        String query = "SELECT name, uuid FROM players;";
        JSONObject obj = new JSONObject();
        JSONArray array = new JSONArray();
        try (Statement stmt = ConnectDB.getConnection().createStatement()) {
            array = new JSONArray();
            result = stmt.executeQuery(query);
            while (result.next()) {
                obj = new JSONObject();
                obj.put("uuid", result.getString("uuid"));
                obj.put("name", result.getString("name"));
                array.add(obj);
            }
        } catch (SQLException e) {
            // getLogger().log(INFO, ChatColor.YELLOW + "Error at Query.dump");
            e.printStackTrace();
        }
        return array;
    }

}
