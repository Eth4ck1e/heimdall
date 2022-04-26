package com.bifrostsmp.heimdall;

import com.bifrostsmp.heimdall.config.CreateConfig;
import com.bifrostsmp.heimdall.config.Parser;
import com.bifrostsmp.heimdall.database.ConnectDB;
import com.bifrostsmp.heimdall.database.CreateDB;
import com.bifrostsmp.heimdall.discord.applications.AppHandler;
import com.bifrostsmp.heimdall.discord.commands.Info;
import com.bifrostsmp.heimdall.discord.commands.PingPong;
import com.bifrostsmp.heimdall.discord.commands.SlashCommands;
import com.bifrostsmp.heimdall.discord.commands.Whitelist;
import com.google.inject.Inject;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
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
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.slf4j.Logger;
import org.yaml.snakeyaml.Yaml;

import javax.security.auth.login.LoginException;
import java.nio.file.Path;
import java.sql.Connection;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

@Plugin(
    id = "heimdall-velocity",
    name = "Heimdall",
    version = BuildConstants.VERSION,
    description = "Main plugin which includes discord bot",
    url = "bifrostsmp.com",
    authors = {"Eth4ck1e", "HunnaG"})
public class HeimdallVelocity extends ListenerAdapter {

  private static final EventWaiter eventWaiter = new EventWaiter();
  private final ProxyServer proxy;
  private final Yaml config;
  Object luckPermsApi;

  static HeimdallVelocity instance;

  public static String prefix = "/"; // command prefix

  public static Logger logger;
  public static Connection connection; // This is the variable used to connect to the DB

  @Getter private static JDA discordBot;

  private static Path dataDirectory = null;

  @Inject
  public HeimdallVelocity(
      ProxyServer proxy, Logger logger, @DataDirectory final Path dataDirectory) {
    this.proxy = proxy;
    HeimdallVelocity.logger = logger;
    config = CreateConfig.loadConfig(dataDirectory);
    HeimdallVelocity.dataDirectory = dataDirectory;
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
    final String discordToken = Parser.getDiscordToken(); // get discord token from config.yml

    if (discordToken == null) { // check if token exists, if token exists try to initiate bot
      logger.error(
          "A discord bot token is required to run this plugin, please place your discord bot token from the discord developer portal in the config.yml");
      return;
    }

    try { // try catch to get any errors
      discordBot =
          JDABuilder.createDefault(discordToken)
              .enableIntents(GatewayIntent.GUILD_MEMBERS)
              .setMemberCachePolicy(MemberCachePolicy.ALL)
              .build();
    } catch (LoginException e) {
      e.printStackTrace();
    }
    // Event listeners for commands these are standard commands Slash commands are below
    discordBot.getGuildById(Parser.getDiscordId());
    // These commands take up to an hour to be activated after creation/update/delete
    CommandListUpdateAction commands = discordBot.updateCommands();

    // Simple reply commands
    commands.addCommands(
        Commands.slash("repeat", "Makes the bot say what you tell it to")
            .addOption(
                STRING,
                "content",
                "What the bot should say",
                true) // you can add required options like this too
        );
    commands.addCommands(
        Commands.slash(
                "whitelist",
                "Add, remove, or update the whitelist.\nSyntax is /whitelist add/remove player, /whitelist update.")
            .addSubcommands(
                new SubcommandData("add", "add player to whitelist. /whitelist add player")
                    .addOptions(
                        new OptionData(STRING, "player", "the player to be added")
                            .setRequired(true)))
            .addSubcommands(
                new SubcommandData(
                        "remove", "remove player from whitelist. /whitelist remove player")
                    .addOptions(
                        new OptionData(STRING, "player", "player to be removed").setRequired(true)))
            .addSubcommands(
                new SubcommandData(
                    "update", "updates whitelist to ensure servers are in sync with database")));
    commands.addCommands(
        Commands.slash("apply", "command to apply to the server")
            .addSubcommands(new SubcommandData("staff", "apply for staff"))
            .addSubcommands(new SubcommandData("whitelist", "apply for minecraft server")));
    commands.addCommands(
            Commands.slash("info", "replies with info about the bot")
    );
    commands.addCommands(
            Commands.slash("ping", "play Ping Pong with the Bot!")
    );

    discordBot.addEventListener(
        new Info(),
        new PingPong(),
        new Whitelist(),
        new SlashCommands(),
        eventWaiter,
        new AppHandler());

    // Send the new set of commands to discord, this will override any existing global commands with
    // the new set provided here
    commands.queue();
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

  public static Path getDataDirectory() {
    return dataDirectory;
  }

  public static EventWaiter getEventWaiter() {
    return eventWaiter;
  }
}
