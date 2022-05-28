package com.bifrostsmp.heimdall.minecraft.commands;

import com.bifrostsmp.heimdall.HeimdallPaper;
import com.bifrostsmp.heimdall.mojangAPI.NameToID;
import database.Query;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Whitelist implements CommandExecutor {

    String name;
    String id;

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command cmd,
            @NotNull String label,
            @NotNull String[] args) {

        if (!cmd.getName().equalsIgnoreCase("whitelist")) return false;

        String command = args[0].toLowerCase();

        switch (command) {
            case "backup" -> {
                //TODO remove the update command arg to be replaced with backup.  This will dump the whitelist to a local whitelist.json file.
            }
            case "reload" -> {
                HeimdallPaper.instance.getServer().reloadWhitelist();
                sender.sendMessage(ChatColor.GREEN + "Reloaded Whitelist");
            }
            case "add" -> {
                name = args[1];
                id = NameToID.nameToID(name);
                if (Query.checkPlayer(id)) {
                    sender.sendMessage(ChatColor.RED + name + " is already Whitelisted");
                } else {
                    int result = Query.insertPlayer(name, id);
                    // System.out.println(result);
                    if (result == 0) {
                        sender.sendMessage(
                                ChatColor.RED + "[ERROR] " + name + " could not be added to the database");
                    } else {
                        sender.sendMessage(ChatColor.GREEN + name + " has been added to the Whitelist");
                        Query.updateTrigger();
                    }
                }
            }
            case "remove" -> {
                name = args[1];
                id = NameToID.nameToID(name);
                if (Query.checkPlayer(id)) {
                    int result = Query.removePlayer(id);
                    if (result == 0) {
                        sender.sendMessage(
                                ChatColor.RED + "[ERROR] " + name + " could not be removed from the database");
                    } else {
                        sender.sendMessage(ChatColor.GREEN + name + " has been removed from the Whitelist");
                        Query.updateTrigger();
                    }
                }
            }
            default -> {
                sender.sendMessage("improper syntax");
            }
        }
        return true;
    }
}
