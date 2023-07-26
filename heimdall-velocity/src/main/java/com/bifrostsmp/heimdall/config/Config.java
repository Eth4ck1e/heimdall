package com.bifrostsmp.heimdall.config;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.bifrostsmp.heimdall.HeimdallVelocity.getDataDirectory;


public class Config {

    private static boolean debug;
    private static String user;
    private static String password;
    private static String host;
    private static String port;
    private static String database;
    private static String discordToken;
    private static String discordId;
    private static String botClientId;
    private static String staffRole;

    //Non-config variables
    private static InputStream inputStream;
    private static String url;

    public static void parse(Path dataDirectory) {
        // yaml parser for config.yml
        try {
            inputStream = new FileInputStream(new File(dataDirectory + "/config.yml"));
        } catch (FileNotFoundException | SecurityException e) {
            e.printStackTrace();
        }

        Yaml yaml = new Yaml();
        Map<String, Object> getData = yaml.load(inputStream); // map config data to getData object
        // assign variables for MySQL connection
        debug = (boolean) getData.get("debug");
        user = (String) getData.get("user");
        password = (String) getData.get("password");
        host = (String) getData.get("host");
        port = String.valueOf(getData.get("port"));
        database = (String) getData.get("database");
        url =
                "jdbc:mysql://"
                        + host
                        + ":"
                        + port
                        + "/"
                        + database;
        discordToken = (String) getData.get("discordToken");
        staffRole = (String) getData.get("staffRole");
        botClientId = (String) getData.get("botClientId");
        discordId = (String) getData.get("discordId");
    }

    public static void build() {
        Map<String, Object> dataMap = new LinkedHashMap<>();

        dataMap.put("debug", isHeimdallDebug());

        dataMap.put("user", getUser());
        dataMap.put("password", getPassword());
        dataMap.put("host", getHost());
        dataMap.put("port", getPort());
        dataMap.put("database", getDatabase());

        dataMap.put("discordToken", getDiscordToken());
        dataMap.put("discordId", getDiscordId());
        dataMap.put("botClientId", getBotClientId());

        dataMap.put("staffRole", getStaffRole());

        File file = new File(String.valueOf(getDataDirectory()), "config.yml");
        try {
            PrintWriter writer = new PrintWriter(file);
            DumperOptions options = new DumperOptions();
            options.setIndent(2);
            options.setPrettyFlow(true);
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml yaml = new Yaml();
            yaml.dump(dataMap, writer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        parse(getDataDirectory());
    }

    public static boolean isHeimdallDebug() {
        return debug;
    }

    public static void reloadConfig() {
        parse(getDataDirectory());
    }

    public static String getUser() {
        return user;
    }

    public static void setUser(String user) {
        Config.user = user;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        Config.password = password;
    }

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        Config.url = url;
    }

    public static String getDiscordToken() {
        return discordToken;
    }

    public static void setDiscordToken(String discordToken) {
        Config.discordToken = discordToken;
    }

    public static String getStaffRole() {
        return staffRole;
    }

    public static void setStaffRole(String staffRole) {
        Config.staffRole = staffRole;
    }

    public static String getBotClientId() {
        return botClientId;
    }

    public static void setBotClientId(String botClientId) {
        Config.botClientId = botClientId;
    }

    public static String getDiscordId() {
        return discordId;
    }

    public static void setDiscordId(String discordId) {
        Config.discordId = discordId;
    }

    public static String getDatabase() {
        return database;
    }

    public static String getHost() {
        return host;
    }

    public static String getPort() {
        return port;
    }

    public static void setInputStream(InputStream inputStream) {
        Config.inputStream = inputStream;
    }
}
