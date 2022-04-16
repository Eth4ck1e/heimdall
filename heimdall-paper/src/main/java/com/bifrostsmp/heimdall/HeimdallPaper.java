package com.bifrostsmp.heimdall;

import com.bifrostsmp.heimdall.database.ConnectDB;
import com.bifrostsmp.heimdall.database.CreateDB;
import com.bifrostsmp.heimdall.database.Query;
import com.bifrostsmp.heimdall.scheduler.ScheduledTask;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.util.Timer;

public final class HeimdallPaper extends JavaPlugin {

  public static Plugin plugin;

  private final String user = getConfig().getString("database.user");
  private final String password = getConfig().getString("database.password");
  private final String url =
          "jdbc:mysql://"
                  + getConfig().getString("database.host")
                  + ":"
                  + getConfig().getString("database.port")
                  + "/"
                  + getConfig().getString("database.database");
  private final String server = getConfig().getString("server");
  public static HeimdallPaper instance;

  @Override
  public void onEnable() {
    // Plugin startup logic
    super.onEnable();
    instance = this;

    saveDefaultConfig();

    if (ConnectDB.connectDB(user, password, url)) {
      getLogger().info(ChatColor.GREEN + "mySQL database connection successful!");
    } else {
      getLogger().warning(ChatColor.RED + "Could not connect to database");
    }
    CreateDB.createDB();
    if (Query.insert(server)) {
      getLogger().info(ChatColor.GREEN + "MySQL insert successful!");
    } else {
      getLogger().warning(ChatColor.RED + "Could not insert into database");
    }

    Timer time = new Timer();
    ScheduledTask st = new ScheduledTask(server);
    time.schedule(st, 0, 30000);

  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
    Connection connection = ConnectDB.connection;

    try {
      if (connection != null && !connection.isClosed()) {
        // avoid receiving a null pointer
        connection.close(); // closes the connection
      }
    } catch (Exception e) {
      //getLogger().log(INFO, ChatColor.YELLOW + "Error onDisable");
      e.printStackTrace();
    }
    getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "[Heimdall] Plugin is disabled");
  }

}
