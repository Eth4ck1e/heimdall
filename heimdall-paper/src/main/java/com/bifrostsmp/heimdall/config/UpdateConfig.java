package com.bifrostsmp.heimdall.config;

import com.bifrostsmp.heimdall.HeimdallPaper;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class UpdateConfig extends JavaPlugin {
  public static void updateConfig() {
    HeimdallPaper plugin =
        (HeimdallPaper) Bukkit.getPluginManager().getPlugin(String.valueOf(HeimdallPaper.plugin));
    assert plugin != null;
    plugin.getConfig().options().copyDefaults(true);
  }
}
