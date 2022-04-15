package com.bifrostsmp.heimdall.config;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Map;

public class Parse {

  private static String user;
  private static String password;
  private static String url;
  private static String discordToken;
  private static InputStream inputStream;

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
}
