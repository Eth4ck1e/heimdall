package com.bifrostsmp.heimdall.config;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.bifrostsmp.heimdall.HeimdallVelocity.getDataDirectory;

public class ConfigParser {

    private static String user;
    private static String password;
    private static String url;
    private static String discordToken;
    private static InputStream inputStream;
    private static String staffRole;
    private static String appRole;
    private static String botClientId;
    private static String discordId;
    private static String appPending;
    private static String appAccepted;
    private static String appDenied;

    private static String howdyChannel;

    private static String welcomeChannel;

    private static String rulesChannel;

    private static boolean welcomeMessages;

    private static String staffCategory;

    private static String host;
    private static String port;
    private static String database;

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
        appRole = (String) getData.get("appRole");
        botClientId = (String) getData.get("botClientId");
        discordId = (String) getData.get("discordId");
        appPending = (String) getData.get("appPending");
        appAccepted = (String) getData.get("appAccepted");
        appDenied = (String) getData.get("appDenied");
        howdyChannel = (String) getData.get("howdyChannel");
        welcomeChannel = (String) getData.get("welcomeChannel");
        rulesChannel = (String) getData.get("rulesChannel");
        welcomeMessages = (boolean) getData.get("welcomeMessages");
        staffCategory = (String) getData.get("staffCategory");
    }

    public static void build() {
        Map<String, Object> dataMap = new LinkedHashMap<>();
        dataMap.put("discordToken", getDiscordToken());
        dataMap.put("discordId", getDiscordId());
        dataMap.put("botClientId", getBotClientId());
        dataMap.put("staffRole", getStaffRole());
        dataMap.put("appRole", getAppRole());
        dataMap.put("appPending", getAppPending());
        dataMap.put("appAccepted", getAppAccepted());
        dataMap.put("appDenied", getAppDenied());
        dataMap.put("welcomeChannel", getWelcomeChannel());
        dataMap.put("howdyChannel", getHowdyChannel());
        dataMap.put("rulesChannel", getRulesChannel());
        dataMap.put("staffCategory", getStaffCategory());
        dataMap.put("welcomeMessages", getWelcomeMessages());
        dataMap.put("host", getHost());
        dataMap.put("port", getPort());
        dataMap.put("database", getDatabase());
        dataMap.put("user", getUser());
        dataMap.put("password", getPassword());
        File file = new File(String.valueOf(getDataDirectory()), "config.yml");
        try {
            PrintWriter writer = new PrintWriter(file);
            Yaml yaml = new Yaml();
            yaml.dump(dataMap, writer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        parse(getDataDirectory());
    }

    public static void reloadConfig() {
        parse(getDataDirectory());
    }

    public static String getUser() {
        return user;
    }

    public static void setUser(String user) {
        ConfigParser.user = user;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        ConfigParser.password = password;
    }

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        ConfigParser.url = url;
    }

    public static String getDiscordToken() {
        return discordToken;
    }

    public static void setDiscordToken(String discordToken) {
        ConfigParser.discordToken = discordToken;
    }

    public static String getStaffRole() {
        return staffRole;
    }

    public static void setStaffRole(String staffRole) {
        ConfigParser.staffRole = staffRole;
    }

    public static String getAppRole() {
        return appRole;
    }

    public static void setAppRole(String appRole) {
        ConfigParser.appRole = appRole;
    }

    public static String getBotClientId() {
        return botClientId;
    }

    public static void setBotClientId(String botClientId) {
        ConfigParser.botClientId = botClientId;
    }

    public static String getDiscordId() {
        return discordId;
    }

    public static void setDiscordId(String discordId) {
        ConfigParser.discordId = discordId;
    }

    public static String getAppPending() {
        return appPending;
    }

    public static void setAppPending(String appPending) {
        ConfigParser.appPending = appPending;
    }

    public static String getAppAccepted() {
        return appAccepted;
    }

    public static void setAppAccepted(String appAccepted) {
        ConfigParser.appAccepted = appAccepted;
    }

    public static String getAppDenied() {
        return appDenied;
    }

    public static void setAppDenied(String appDenied) {
        ConfigParser.appDenied = appDenied;
    }

    public static String getWelcomeChannel() {
        return welcomeChannel;
    }

    public static void setWelcomeChannel(String welcomeChannel) {
        ConfigParser.welcomeChannel = welcomeChannel;
    }

    public static String getHowdyChannel() {
        return howdyChannel;
    }

    public static void setHowdyChannel(String howdyChannel) {
        ConfigParser.howdyChannel = howdyChannel;
    }

    public static String getRulesChannel() {
        return rulesChannel;
    }

    public static void setRulesChannel(String rulesChannel) {
        ConfigParser.rulesChannel = rulesChannel;
    }

    public static boolean getWelcomeMessages() {
        return welcomeMessages;
    }

    public static void setWelcomeMessages(boolean welcomeMessages) {
        ConfigParser.welcomeMessages = welcomeMessages;
    }

    public static String getStaffCategory() {
        return staffCategory;
    }

    public static void setStaffCategory(String staffCategory) {
        ConfigParser.staffCategory = staffCategory;
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
        ConfigParser.inputStream = inputStream;
    }
}
