package com.bifrostsmp.heimdall;

import com.bifrostsmp.heimdall.config.Config;
import com.bifrostsmp.heimdall.config.CreateConfig;
import com.bifrostsmp.heimdall.discord.buttons.RulesClickMe;
import com.bifrostsmp.heimdall.discord.buttons.TicketClose;
import com.bifrostsmp.heimdall.discord.buttons.application.Accept;
import com.bifrostsmp.heimdall.discord.buttons.application.Deny;
import com.bifrostsmp.heimdall.discord.commands.Apply;
import com.bifrostsmp.heimdall.discord.commands.SlashCommands;
import com.bifrostsmp.heimdall.discord.commands.Welcome;
import com.bifrostsmp.heimdall.discord.commands.set.SetTicketCategory;
import com.bifrostsmp.heimdall.discord.events.Join;
import com.google.inject.Inject;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import database.ConnectDB;
import database.CreateDB;
import io.javalin.Javalin;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import org.slf4j.Logger;
import org.yaml.snakeyaml.Yaml;

import javax.security.auth.login.LoginException;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.sql.Connection;
import java.util.Map;
import java.util.Objects;

import static com.bifrostsmp.heimdall.config.Config.*;
import static com.bifrostsmp.heimdall.html.CreateHTML.loadHTML;
import static net.dv8tion.jda.api.interactions.commands.OptionType.*;

@Plugin(
        id = "heimdall-velocity",
        name = "Heimdall",
        version = BuildConstants.VERSION,
        description = "Main plugin which includes discord bot",
        url = "bifrostsmp.com",
        authors = {"Eth4ck1e"})
public class HeimdallVelocity extends ListenerAdapter {
    public static Logger logger;
    public static Connection connection; // This is the variable used to connect to the DB
    static HeimdallVelocity instance;
    private static JDA discordBot;
    private static Path dataDirectory = null;
    private static Guild guild;
    private static int ticketNumber = 1;
    private final ProxyServer proxy;
    private final Yaml config;

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
        loadHTML(dataDirectory, "webInterface", "index.html");
    }

    public static Path getDataDirectory() {
        return dataDirectory;
    }

    public static Guild getGuild() {
        return guild;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {

        // Create a new Javalin instance
        Javalin app = Javalin.create().start(8080);

        // Endpoint for serving the config file
        app.get("/config", ctx -> {
           // Read the content of the config file
           Path configFilePath = Path.of(dataDirectory + "/config.yml");
           String yamlContent = Files.readString(configFilePath);

           //Set the response content type
           ctx.contentType("application/yaml");

           // Return the config file content
           ctx.result(yamlContent);
        });

        // Define a route that serves the index.html file
        app.get("/", ctx -> {
            try {
                Path filePath = Path.of(dataDirectory.toString(), "webInterface", "index.html");
                InputStream inputStream = new FileInputStream(filePath.toFile());
                ctx.contentType("text/html").result(inputStream);
            } catch (FileNotFoundException | SecurityException e) {
                e.printStackTrace();
                ctx.status(404); // Set a 404 status code if the file is not found
            }
        });

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        Config.parse(getDataDirectory());

        ConnectDB.setUrl(getUrl());
        ConnectDB.setUser(getUser());
        ConnectDB.setPassword(getPassword());

        CreateDB.create(getUser(), getPassword(), getUrl()); // create DB if not exist

        logger.info(
                "Starting Discord Bot"); // send message to console with plugin name, logger tags with
        // plugin name
        final String discordToken = Config.getDiscordToken(); // get discord token from config.yml

        if (discordToken == null) { // check if token exists, if token exists try to initiate bot
            logger.error(
                    "A discord bot token is required to run this plugin, please place your discord bot token from the discord developer portal in the config.yml");
            return;
        }

        try { // Connects to the bot
            discordBot =
                    JDABuilder.createDefault(discordToken)
                            .enableIntents(GatewayIntent.GUILD_MEMBERS)
                            .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                            .setMemberCachePolicy(MemberCachePolicy.ALL)
                            .setActivity(Activity.watching("Everything!"))
                            .build().awaitReady();
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }
        guild = discordBot.getGuildById(Config.getDiscordId());
        // Event listeners for commands these are standard commands Slash commands are below
        // These commands take up to an hour to be activated after creation/update/delete
        CommandListUpdateAction commands = discordBot.updateCommands();

        //BEGIN SLASH COMMANDS
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
                                                new OptionData(STRING, "player", "player to be removed").setRequired(true))));
        commands.addCommands(
                Commands.slash("apply", "command to apply to the server"));
        commands.addCommands(
                Commands.slash("info", "replies with info about the bot")
        );
        commands.addCommands(
                Commands.slash("ping", "play Ping Pong with the Bot!")
        );
        commands.addCommands(
                Commands.slash("ticket", "command to open a new ticket")
        );
        commands.addCommands(
                Commands.slash("invite", "grants specified member perms to the current channel")
                        .addOption(USER, "user", "the mentioned user to be invited to the channel")
        );
        commands.addCommands(
                Commands.slash("set", "Sets channels and categories for bot")
                        .addSubcommandGroups(
                                new SubcommandGroupData("application", "sets channels for applications")
                                        .addSubcommands(
                                                new SubcommandData("accepted", "sets channel for accepted applications"),
                                                new SubcommandData("denied", "sets channel for denied applications"),
                                                new SubcommandData("pending", "sets channel for pending applications"),
                                                new SubcommandData("role", "sets the role for application command use").addOption(ROLE, "role", "the mentioned role to be added")
                                        )
                        )
                        .addSubcommands(new SubcommandData("tickets", "set tickets category"))
                        .addSubcommands(new SubcommandData("staff", "sets the role that can use all commands").addOption(ROLE, "role", "the mentioned role to be added"))
                        .addSubcommands(new SubcommandData("welcome-channel", "sets the welcome channel for posting server welcome information"))
                        .addSubcommands(new SubcommandData("howdy-channel", "sets the channel where members will get custom welcome messages upon joining"))
                        .addSubcommands(new SubcommandData("rules-channel", "set the channel where rules are posted"))
        );
        commands.addCommands(
                Commands.slash("welcome",
                        "enables or disables bot to welcome members")
        );
        commands.addCommands(
                Commands.slash("setup",
                        "Run command to setup the bot (you must be owner if this is the first time)")
        );
        commands.addCommands(
                Commands.slash("reload",
                        "reloads the bot config in memory after manual config changes")
        );
        //END SLASH COMMANDS

        // Register event listeners
        discordBot.addEventListener(
                new SlashCommands(),
                new Accept(),
                new Deny(),
                new RulesClickMe(),
                new TicketClose(),
                new SetTicketCategory(),
                new Welcome(),
                new Apply()
        );
        if (getWelcomeMessages()) {
            discordBot.addEventListener(
                    new Join()
            );
        }

        // Send the new set of commands to discord,
        // this will override any existing global commands with the new set provided here
        commands.queue();

//    PostWelcome.postWelcome();
//    PostRules.postRules();
    }

    @Subscribe(order = PostOrder.LATE)
    public void onDisable(ProxyShutdownEvent event) {
        // Plugin shutdown logic

//    PostRules.getDiscordMessage().delete().complete();
//    PostRules.getMinecraftMessage().delete().complete();
//    PostRules.getFinalMessage().delete().complete();
//    PostWelcome.getWelcomeMessage().delete().complete();
        getDiscordBot().shutdown();
        try {
            if (connection != null && !connection.isClosed()) {
                // avoid receiving a null pointer
                connection.close(); // closes the connection
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JDA getDiscordBot() {
        return discordBot;
    }
}
