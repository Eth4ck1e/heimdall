package com.bifrostsmp.heimdall.config;

import org.checkerframework.checker.units.qual.C;
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
    private static String appRole;
    private static String appPending;
    private static String appAccepted;
    private static String appDenied;
    private static String howdyChannel;
    private static String welcomeChannel;
    private static String rulesChannel;
    private static boolean welcomeMessages;
    private static String staffCategory;
    private static Map<String, String> applications;
    private static Map<String, String> applicationDenyOptions;

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
        applications = (Map<String, String>) getData.get("applications");
        applicationDenyOptions = (Map<String, String>) getData.get("applicationDenyOptions");
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
        dataMap.put("appRole", getAppRole());

        dataMap.put("appPending", getAppPending());
        dataMap.put("appAccepted", getAppAccepted());
        dataMap.put("appDenied", getAppDenied());

        dataMap.put("howdyChannel", getHowdyChannel());
        dataMap.put("welcomeChannel", isWelcomeChannel());
        dataMap.put("rulesChannel", getRulesChannel());

        dataMap.put("welcomeMessages", getWelcomeMessages());
        dataMap.put("staffCategory", getStaffCategory());

        dataMap.put("applications", getApplications());
        dataMap.put("applicationDenyOptions", getApplicationDenyOptions());


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

    public static Map<String, String> getApplicationDenyOptions() {
        return applicationDenyOptions;
    }

    public static void setApplicationDenyOptions(Map<String, String> applicationDenyOptions) {
        Config.applicationDenyOptions = applicationDenyOptions;
    }

    public static Map<String, String> getApplications() {
        return applications;
    }

    public static void setApplications(Map<String, String> applications) {
        Config.applications = applications;
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

    public static String getAppRole() {
        return appRole;
    }

    public static void setAppRole(String appRole) {
        Config.appRole = appRole;
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

    public static String getAppPending() {
        return appPending;
    }

    public static void setAppPending(String appPending) {
        Config.appPending = appPending;
    }

    public static String getAppAccepted() {
        return appAccepted;
    }

    public static void setAppAccepted(String appAccepted) {
        Config.appAccepted = appAccepted;
    }

    public static String getAppDenied() {
        return appDenied;
    }

    public static void setAppDenied(String appDenied) {
        Config.appDenied = appDenied;
    }

    public static String isWelcomeChannel() {
        return welcomeChannel;
    }

    public static void setWelcomeChannel(String welcomeChannel) {
        Config.welcomeChannel = welcomeChannel;
    }

    public static String getHowdyChannel() {
        return howdyChannel;
    }

    public static void setHowdyChannel(String howdyChannel) {
        Config.howdyChannel = howdyChannel;
    }

    public static String getRulesChannel() {
        return rulesChannel;
    }

    public static void setRulesChannel(String rulesChannel) {
        Config.rulesChannel = rulesChannel;
    }

    public static boolean getWelcomeMessages() {
        return welcomeMessages;
    }

    public static void setWelcomeMessages(boolean welcomeMessages) {
        Config.welcomeMessages = welcomeMessages;
    }

    public static String getStaffCategory() {
        return staffCategory;
    }

    public static void setStaffCategory(String staffCategory) {
        Config.staffCategory = staffCategory;
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
