package com.bifrostsmp.heimdall.config;

import static com.bifrostsmp.heimdall.HeimdallPaper.instance;

public class ConfigParser {

    private static boolean debug;
    private static boolean hardcore;
    private static int tempBan;
    private static boolean firstRun;
    private static String server;
    private static String host;
    private static String port;
    private static String database;
    private static String user;
    private static String password;
    private static String url;

    public static void parse() {
        debug = instance.getConfig().getBoolean("debug");
        hardcore = instance.getConfig().getBoolean("hardcore");
        tempBan = instance.getConfig().getInt("tempBan");
        firstRun = instance.getConfig().getBoolean("firstRun");
        server = instance.getConfig().getString("server");
        host = instance.getConfig().getString("database.host");
        port = instance.getConfig().getString("database.port");
        database = instance.getConfig().getString("database.database");
        user = instance.getConfig().getString("database.user");
        password = instance.getConfig().getString("database.password");
        url =
                "jdbc:mysql://"
                        + host
                        + ":"
                        + port
                        + "/"
                        + database;
    }

    public static boolean isConfigDebug() {
        return debug;
    }

    public static void setConfigDebug(boolean debug) {
        ConfigParser.debug = debug;
    }

    public static boolean isConfigHardcore() {
        return hardcore;
    }

    public static void setConfigHardcore(boolean hardcore) {
        ConfigParser.hardcore = hardcore;
    }

    public static int getTempBan() {
        return tempBan;
    }

    public static void setTempBan(int tempBan) {
        ConfigParser.tempBan = tempBan;
    }

    public static boolean isFirstRun() {
        return firstRun;
    }

    public static void setFirstRun(boolean firstRun) {
        ConfigParser.firstRun = firstRun;
    }

    public static String getConfigServer() {
        return server;
    }

    public static void setConfigServer(String server) {
        ConfigParser.server = server;
    }

    public static String getHost() {
        return host;
    }

    public static void setHost(String host) {
        ConfigParser.host = host;
    }

    public static String getPort() {
        return port;
    }

    public static void setPort(String port) {
        ConfigParser.port = port;
    }

    public static String getDatabase() {
        return database;
    }

    public static void setDatabase(String database) {
        ConfigParser.database = database;
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
}
