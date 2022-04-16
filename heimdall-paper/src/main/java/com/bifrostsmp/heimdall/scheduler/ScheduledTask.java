package com.bifrostsmp.heimdall.scheduler;

import com.bifrostsmp.heimdall.HeimdallPaper;
import com.bifrostsmp.heimdall.database.Query;
import com.bifrostsmp.heimdall.jason.WhitelistBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.json.simple.JSONArray;

import java.util.Date;
import java.util.TimerTask;

public class ScheduledTask extends TimerTask {
    private final String server;
    Date now;

    public ScheduledTask(String server) {
        this.server = server;
    }

    public void run() {
        if(!Query.check(server)) { //run check to see if JSONUpdated is false
            JSONArray result = Query.dump();  //result of query dump
            //getLogger().log(INFO, ChatColor.YELLOW + "updating whitelist");
            WhitelistBuilder.whitelistBuilder(result);
            Bukkit.getScheduler().scheduleSyncDelayedTask(HeimdallPaper.instance, new Runnable() {
                @Override
                public void run() {
                    ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                    String command = "whitelist reload";
                    Bukkit.dispatchCommand(console, command);
                }
            }, 20L); //20 Tick (1 Second) delay before run() is called
            Query.updated(server);  //change JSONUpdated to true
        } else {
            //getLogger().log(INFO, ChatColor.YELLOW + "No need to update whitelist");
            now = new Date();
            return;
        }
        now = new Date();
    }

}
