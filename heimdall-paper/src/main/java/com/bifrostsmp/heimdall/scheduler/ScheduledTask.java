package com.bifrostsmp.heimdall.scheduler;

import com.bifrostsmp.heimdall.HeimdallPaper;
import com.bifrostsmp.heimdall.database.Query;
import com.bifrostsmp.heimdall.json.WhitelistBuilder;
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
    if (!Query.check(server)) { // run check to see if JSONUpdated is false
      JSONArray result = Query.dump(); // result of query dump
      // getLogger().log(INFO, ChatColor.YELLOW + "updating whitelist");
      WhitelistBuilder.whitelistBuilder(result);
      HeimdallPaper.instance.getServer().reloadWhitelist();
      Query.updated(server); // change JSONUpdated to true
      HeimdallPaper.instance.getServer().getConsoleSender().sendMessage("[Heimdall]Whitelist was updated and reloaded");
    } else {
      // getLogger().log(INFO, ChatColor.YELLOW + "No need to update whitelist");
      now = new Date();
      return;
    }
    now = new Date();
  }
}
