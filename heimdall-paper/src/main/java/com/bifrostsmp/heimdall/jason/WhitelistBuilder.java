package com.bifrostsmp.heimdall.jason;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.json.simple.JSONArray;

import java.io.FileWriter;

public class WhitelistBuilder {
    public static void whitelistBuilder(JSONArray result) {
        //FileWriter file = new FileWriter(new File("./whitelist.json").getAbsolutePath(),false);
        String path = "./whitelist.json";
        //getLogger().log(INFO, ChatColor.YELLOW + "JSON variable: " + result);
        //getLogger().log(INFO, ChatColor.YELLOW + "Writing file");


        try (FileWriter out = new FileWriter(path)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(result.toJSONString());
            String prettyJsonString = gson.toJson(je);
            out.write(prettyJsonString);
        } catch (Exception e) {
            //getLogger().log(INFO, ChatColor.YELLOW + "error writing/writing-to whitelist.json");
            e.printStackTrace();
        }
    }
}

