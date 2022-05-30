package database;

import org.bukkit.ChatColor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.*;
import java.util.Objects;

import static java.util.logging.Level.INFO;
import static org.bukkit.Bukkit.getLogger;

public class Query {
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

    public static int insertPlayer(String name, String id) {
        String insertSQL = "INSERT IGNORE INTO players(name, uuid) VALUES (?,?);";
        try (Connection connection = ConnectDB.getConnection()) {
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

    public static boolean check(String server) {
        boolean value = false;
        String query = "SELECT JSONUpdated FROM servers WHERE server = ?;";
        try (Connection connection = ConnectDB.getConnection()) {
            PreparedStatement check = connection.prepareStatement(query);
            check.setString(1, server);
            ResultSet result = check.executeQuery();
            value = result.getObject(1, Boolean.class);
            // getLogger().log(INFO, ChatColor.YELLOW + "[Heimdall] current JSONUpdated value from check:
            // " + value);
        } catch (SQLException e) {
            // getLogger().log(INFO, ChatColor.YELLOW + "Error at Query.check");
            e.printStackTrace();
        }
        return value;
    }

    public static void updateTrigger() {
        String query = "UPDATE servers SET JSONUpdated = false WHERE JSONUpdated = true;";
        try (Connection connection = ConnectDB.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updated(String server) {
        String query = "UPDATE servers SET JSONUpdated = true WHERE server=?;";
        try (Connection connection = ConnectDB.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, server);
            stmt.executeUpdate();
        } catch (SQLException e) {
            // getLogger().log(INFO, ChatColor.YELLOW + "Error at Query.updated");
            e.printStackTrace();
        }
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

    public static int getAppCounter(long discordID) {
        String get = "SELECT counter FROM applications WHERE discordID = ?;";
        try (Connection connection = ConnectDB.getConnection()) {
            PreparedStatement getAppCounter = connection.prepareStatement(get);
            getAppCounter.setString(1, String.valueOf(discordID));
            ResultSet rs = getAppCounter.executeQuery();
            if (!rs.next()) return 0;
            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void insertApp(long discordID, String ign, String uuid, String app, int counter) {
        String insertApp =
                "INSERT INTO applications(discordID, ign, uuid, app, counter) VALUES (?, ?, ?, ?, ?);";
        try (Connection connection = ConnectDB.getConnection()) {
            PreparedStatement insert = connection.prepareStatement(insertApp);
            insert.setString(1, String.valueOf(discordID));
            insert.setString(2, ign);
            insert.setString(3, uuid);
            insert.setString(4, app);
            insert.setInt(5, counter);

            insert.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateApp(long discordID, String app, int counter) {
        String updateApp = "UPDATE applications SET app=?, counter=? WHERE discordID=?;";
        try (Connection connection = ConnectDB.getConnection()) {
            PreparedStatement update = connection.prepareStatement(updateApp);
            update.setString(1, app);
            update.setInt(2, counter);
            update.setString(3, String.valueOf(discordID));
            update.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkForApp(long discordID) {
        String check = "SELECT * FROM applications WHERE discordID = " + discordID + ";";
        try (Connection connection = ConnectDB.getConnection()) {
            PreparedStatement checkForApp = connection.prepareStatement(check);
            return checkForApp.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int getTicketNum() {
        String ticketNum = "select max(ticketNum) from tickets;";
        try (Connection connection = ConnectDB.getConnection()) {
            PreparedStatement getTicketNum = connection.prepareStatement(ticketNum);
            return getTicketNum.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void newTicket(String name) {
        String insert = "insert into tickets(name) values(?);";
        try (Connection connection = ConnectDB.getConnection()) {
            PreparedStatement newTicket = connection.prepareStatement(insert);
            newTicket.setString(1, name);
            newTicket.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
