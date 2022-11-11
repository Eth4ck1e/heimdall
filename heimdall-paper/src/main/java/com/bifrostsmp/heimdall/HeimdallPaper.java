package com.bifrostsmp.heimdall;

import com.bifrostsmp.heimdall.config.ConfigParser;
import com.bifrostsmp.heimdall.minecraft.commands.Whitelist;
import com.bifrostsmp.heimdall.minecraft.events.HardcoreDeath;
import com.bifrostsmp.heimdall.minecraft.events.PreLoginWhitelistCheck;
import database.ConnectDB;
import database.CreateDB;
import database.Query;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;

import static com.bifrostsmp.heimdall.config.ConfigParser.*;
import static org.bukkit.Bukkit.isHardcore;

public final class HeimdallPaper extends JavaPlugin {
    public static Plugin plugin;
    public static HeimdallPaper instance;
    public static boolean debug;
    public static HeimdallPaper getInstance() {
        return instance;
    }

    public static Plugin getPlugin() {
        return plugin;
    }
    private void setDebug(boolean debug) {
        HeimdallPaper.debug = debug;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        super.onEnable();
        instance = this;
        parse();

        saveDefaultConfig();

        if (ConnectDB.connectDB(getUser(), getPassword(), getUrl())) {
            ConnectDB.setUser(getUser());
            ConnectDB.setPassword(getPassword());
            ConnectDB.setUrl(getUrl());
            getLogger().info(ChatColor.GREEN + "mySQL database connection successful!");
        } else {
            getLogger().warning(ChatColor.RED + "Could not connect to database");
        }
        CreateDB.create(getUser(), getPassword(), getUrl());
        if (Query.insertServer(getConfigServer())) {
            getLogger().info(ChatColor.GREEN + "MySQL insert successful!");
        } else {
            getLogger()
                    .warning(
                            ChatColor.RED
                                    + "Could not insert into database. Please make sure you have updated the config and changed your server name from default to a unique identifier(different from any other server you have connected to the database");
        }
        getCommand("whitelist").setExecutor(new Whitelist());
        getServer().getPluginManager().registerEvents(new PreLoginWhitelistCheck(), this);
        if (isConfigHardcore()) {
            getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "[Heimdall] Hardcore mode is enabled");
            getServer().getPluginManager().registerEvents(new HardcoreDeath(), this);
        }
        setDebug(getConfig().getBoolean("debug"));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Connection connection = ConnectDB.getConnection();

        try {
            if (connection != null && !connection.isClosed()) {
                // avoid receiving a null pointer
                connection.close(); // closes the connection
            }
        } catch (Exception e) {
            // getLogger().log(INFO, ChatColor.YELLOW + "Error onDisable");
            e.printStackTrace();
        }
        getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "[Heimdall] Plugin is disabled");
    }

}
