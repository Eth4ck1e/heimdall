package whitelist;


import database.Query;
import json.WhitelistBuilder;
import org.json.simple.JSONArray;

public class BackupWhitelist {
    public static void backup() {
        JSONArray result = Query.dump(); // result of query dump
        // getLogger().log(INFO, ChatColor.YELLOW + "updating whitelist");
        WhitelistBuilder.whitelistBuilder(result);
    }
}
