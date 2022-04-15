package com.bifrostsmp.heimdall.jason;

import org.json.simple.JSONArray;

import java.io.FileWriter;
import java.io.PrintWriter;

public class WhitelistBuilder {
    public static void whitelistBuilder(JSONArray result) {
        //FileWriter file = new FileWriter(new File("./whitelist.json").getAbsolutePath(),false);
        String path = "./whitelist.json";
        System.out.println("Printing JSON variable:");
        System.out.println(result);
        System.out.println("Writing file");
        try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
            out.write(result.toJSONString());
        } catch (Exception e) {
            System.out.println("error writing/writing-to whitelist.json");
            e.printStackTrace();
        }
    }
}

