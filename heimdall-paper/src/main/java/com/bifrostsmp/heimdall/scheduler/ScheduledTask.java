package com.bifrostsmp.heimdall.scheduler;

import com.bifrostsmp.heimdall.database.Query;
import com.bifrostsmp.heimdall.jason.WhitelistBuilder;
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
            System.out.println("updating whitelist");
            WhitelistBuilder.whitelistBuilder(result);
            Query.updated(server);  //change JSONUpdated to true
        } else {
            System.out.println("No need to update whitelist");
        }
        now = new Date();
    }

}
