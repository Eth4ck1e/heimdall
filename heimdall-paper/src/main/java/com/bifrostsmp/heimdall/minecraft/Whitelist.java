package com.bifrostsmp.heimdall.minecraft;

import com.bifrostsmp.heimdall.HeimdallPaper;
import com.bifrostsmp.heimdall.database.ConnectDB;
import com.bifrostsmp.heimdall.database.Query;
import com.bifrostsmp.heimdall.mojangAPI.NameToID;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Whitelist implements CommandExecutor {

  @Override
  public boolean onCommand(
      @NotNull CommandSender sender,
      @NotNull Command cmd,
      @NotNull String label,
      @NotNull String[] args) {

    if (!cmd.getName().equalsIgnoreCase("whitelist")) return false;
    // TODO code for whitelisting
    if (args.length == 1 && args[0].equalsIgnoreCase("update")) {
      ConnectDB.connectDB(
          HeimdallPaper.instance.getUser(),
          HeimdallPaper.instance.getPassword(),
          HeimdallPaper.instance.getUrl());
      Query.update();
      sender.sendMessage(ChatColor.GREEN + "Whitelist update started(Takes up to 30 Seconds)");
    } else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
      HeimdallPaper.instance.getServer().reloadWhitelist();
      sender.sendMessage(ChatColor.GREEN + "Reloaded Whitelist");
    } else if (args.length == 2
        && (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove"))) {
      if (args[0].equalsIgnoreCase("add")) {
        String name = args[1];
        String id = NameToID.nameToID(name);
        if (Query.checkPlayer(id)) {
          sender.sendMessage(ChatColor.RED + name + " is already Whitelisted");
        } else {
          int result = Query.insertPlayers(name, id);
          // System.out.println(result);
          if (result == 0) {
            sender.sendMessage(
                ChatColor.RED + "[ERROR] " + name + " could not be added to the database");
          } else {
            sender.sendMessage(ChatColor.GREEN + name + " has been added to the Whitelist");
            Query.update();
          }
        }
      }
      if (args[0].equalsIgnoreCase("remove")) {
        String name = args[1];
        String id = NameToID.nameToID(name);
        if (Query.checkPlayer(id)) {
          int result = Query.removePlayers(id);
          if (result == 0) {
            sender.sendMessage(
                ChatColor.RED + "[ERROR] " + name + " could not be removed from the database");
          } else {
            sender.sendMessage(ChatColor.GREEN + name + " has been removed from the Whitelist");
            Query.update();
          }
        }
      }
    } else {
      sender.sendMessage("improper syntax");
    }
    return true;
  }
}
