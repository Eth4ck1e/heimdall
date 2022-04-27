package com.bifrostsmp.heimdall.config;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Map;

public class Parser {

  private static String user;
  private static String password;
  private static String url;
  private static String discordToken;
  private static InputStream inputStream;
  private static String staffRole;
  private static String appRole;
  private static String BOT_CLIENT_ID;
  private static String DISCORD_ID;
  private static String appPending;
  private static String appAccepted;
  private static String appDenied;

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
    url =
        "jdbc:mysql://"
            + getData.get("host")
            + ":"
            + getData.get("port")
            + "/"
            + getData.get("database");
    discordToken = (String) getData.get("DISCORD_TOKEN");
    staffRole = (String) getData.get("STAFF_ROLE");
    appRole = (String) getData.get("APPLICANT_ROLE");
    BOT_CLIENT_ID = (String) getData.get("BOT_CLIENT_ID");
    DISCORD_ID = (String) getData.get("DISCORD_ID");
    appPending = (String) getData.get("appPending");
    appAccepted = (String) getData.get("appAccepted");
    appDenied = (String) getData.get("appDenied");
  }

  public static String getUser() {
    return user;
  }

  public static String getPassword() {
    return password;
  }

  public static String getUrl() {
    return url;
  }

  public static String getDiscordToken() {
    return discordToken;
  }

  public static String getStaffRole() {
    return staffRole;
  }

  public static String getAppRole() { return appRole; }

  public static String getBotClientId() {
    return BOT_CLIENT_ID;
  }

  public static String getDiscordId() {
    return DISCORD_ID;
  }

  public static String getAppPending() {
    return appPending;
  }

  public static String getAppAccepted() {
    return appAccepted;
  }

  public static String getAppDenied() {
    return appDenied;
  }
}
