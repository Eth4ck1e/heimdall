package com.bifrostsmp.heimdall.minecraft.commands;

import com.bifrostsmp.heimdall.HeimdallPaper;
import com.bifrostsmp.heimdall.database.ImportWhitelist;
import com.bifrostsmp.heimdall.minecraft.whitelist.Backup;
import com.bifrostsmp.heimdall.mojangAPI.NameToID;
import database.Query;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static java.util.logging.Level.INFO;
import static org.bukkit.Bukkit.getLogger;

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
                Backup.backup();
                sender.sendMessage(ChatColor.GREEN + "Whitelist.json created from database");
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
            case "on" -> {
                HeimdallPaper.instance.getServer().setWhitelist(true);
                sender.sendMessage(ChatColor.GREEN + "Whitelist Enabled");
            }
            case "off" -> {
                HeimdallPaper.instance.getServer().setWhitelist(false);
                sender.sendMessage(ChatColor.GREEN + name + "Whitelist Disabled");
            }
            case "import" -> {
                getLogger().log(INFO, ChatColor.YELLOW + "Importing whitelist.json to database...");
                ImportWhitelist.parser();
                getLogger().log(INFO, ChatColor.YELLOW + "whitelist import complete.");
                sender.sendMessage((ChatColor.GREEN + name + "whitelist import complete"));
            }
            default -> {
                sender.sendMessage("improper syntax");
            }
        }
        return true;
    }
}
