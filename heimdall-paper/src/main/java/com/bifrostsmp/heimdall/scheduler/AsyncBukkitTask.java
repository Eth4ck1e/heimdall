package com.bifrostsmp.heimdall.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class AsyncBukkitTask extends BukkitRunnable {

  private final JavaPlugin plugin;

  public AsyncBukkitTask(JavaPlugin plugin) {
    this.plugin = plugin;
  }

  @Override
  public void run() {
    // task goes here
    plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), "whitelist reload");
  }
}
