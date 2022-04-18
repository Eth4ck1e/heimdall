package com.bifrostsmp.heimdall;

import com.bifrostsmp.heimdall.config.CreateConfig;
import com.bifrostsmp.heimdall.config.Parse;
import com.bifrostsmp.heimdall.database.ConnectDB;
import com.bifrostsmp.heimdall.database.CreateDB;
import com.bifrostsmp.heimdall.discord.commands.Info;
import com.bifrostsmp.heimdall.discord.commands.PingPong;
import com.bifrostsmp.heimdall.discord.commands.WhitelistAdd;
import com.google.inject.Inject;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.slf4j.Logger;
import org.yaml.snakeyaml.Yaml;

import javax.security.auth.login.LoginException;
import java.nio.file.Path;
import java.sql.Connection;

@Plugin(
    id = "heimdall-velocity",
    name = "Heimdall",
    version = BuildConstants.VERSION,
    description = "Main plugin which includes discord bot",
    url = "bifrostsmp.com",
    authors = {"Eth4ck1e", "HunnaG"})
public class HeimdallVelocity {

  private final ProxyServer proxy;
  private final Yaml config;
  Object luckPermsApi;

  static HeimdallVelocity instance;

  public static String prefix = "/"; // command prefix

  public static Logger logger;
  public static Connection connection; // This is the variable used to connect to the DB

  @Getter private static JDA discordBot;

  @Getter private final Path dataDirectory;

  @Inject
  public HeimdallVelocity(
      ProxyServer proxy, Logger logger, @DataDirectory final Path dataDirectory) {
    this.proxy = proxy;
    HeimdallVelocity.logger = logger;
    config = CreateConfig.loadConfig(dataDirectory);
    this.dataDirectory = dataDirectory;
    logger.info(
        "Heimdall uses a mysql database to sync the vanilla minecraft whitelist between all servers on your network."
            + "This requires the heimdall plugin to also be installed on each backend server.");
  }

  @Subscribe
  public void onProxyInitialization(ProxyInitializeEvent event) {

    ConnectDB.connection(dataDirectory); // init DB connection

    CreateDB.create(); // create DB if not exist

    logger.info(
        "Starting Discord Bot"); // send message to console with plugin name, logger tags with
    // plugin name
    final String discordToken = Parse.getDiscordToken(); // get discord token from config.yml

    if (discordToken == null) { // check if token exists, if token exists try to initiate bot
      logger.error(
          "A discord bot token is required to run this plugin, please place your discord bot token from the discord developer portal in the config.yml");
      return;
    }

    try { // try catch to get any errors
      discordBot = JDABuilder.createDefault(discordToken)
              .enableIntents(GatewayIntent.GUILD_MEMBERS)
              .setMemberCachePolicy(MemberCachePolicy.ALL)
              .build();
    } catch (LoginException e) {
      e.printStackTrace();
    }
    // Event listeners for commands
    discordBot.addEventListener(new Info(), new PingPong(), new WhitelistAdd());
  }

  @Subscribe(order = PostOrder.LATE)
  public void onDisable(ProxyShutdownEvent event) {
    // Plugin shutdown logic
    try {
      if (connection != null && !connection.isClosed()) {
        // avoid receiving a null pointer
        connection.close(); // closes the connection
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
