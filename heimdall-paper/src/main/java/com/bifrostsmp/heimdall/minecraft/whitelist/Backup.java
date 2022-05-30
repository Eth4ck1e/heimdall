package com.bifrostsmp.heimdall.minecraft.whitelist;

import com.bifrostsmp.heimdall.HeimdallPaper;
import com.bifrostsmp.heimdall.json.WhitelistBuilder;
import database.Query;
import org.json.simple.JSONArray;

public class Backup {
    public static void backup() {
        JSONArray result = Query.dump(); // result of query dump
        // getLogger().log(INFO, ChatColor.YELLOW + "updating whitelist");
        WhitelistBuilder.whitelistBuilder(result);
        HeimdallPaper.instance
                .getServer()
                .getConsoleSender()
                .sendMessage("[Heimdall] Whitelist.json created from database");
    }
}
