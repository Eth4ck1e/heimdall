package com.bifrostsmp.heimdall.database;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class FirstRunWhitelistParser {
    public static String name;
    public static String uuid;
    public static void parser() {
        try {
            Gson gson = new Gson();

            Reader reader = Files.newBufferedReader(Paths.get("./whitelist.json"));

            Map<?,?> map = gson.fromJson(reader, Map.class);

            for (Map.Entry<?,?> entry : map.entrySet()) {
                //insertPlayers(String.valueOf(entry.getValue()));
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
