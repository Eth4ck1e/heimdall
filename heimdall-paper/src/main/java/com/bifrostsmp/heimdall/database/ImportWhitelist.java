package com.bifrostsmp.heimdall.database;

import database.Query;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ImportWhitelist {
    static String path = "./whitelist.json";

    public static void parser() {
        String str = readFileAsString(path);
        JSONArray array = new JSONArray(str);

        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            String name = object.getString("name");
            String uuid = object.getString("uuid");
            Query.insertPlayer(name, uuid);
            // getLogger().log(INFO, ChatColor.GREEN + "Player: " + name + " " + uuid + " has been added
            // to database");
        }
    }

    public static String readFileAsString(String path) {
        String str = null;
        try {
            str = new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            System.out.println("error at FirstRunWhitelistParser.readFileAsString");
            e.printStackTrace();
        }
        return str;
    }
}
