package database;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ImportWhitelist {
    static String path = "./whitelist.json";

    public static void parser() {
        String str = readFileAsString(path);
        JSONArray array = new JSONArray(str);

        List<String> names = new ArrayList<>();
        List<String> uuids = new ArrayList<>();
        List<Boolean> bools = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            String name = object.getString("name");
            String uuid = object.getString("uuid");

            // Collect the data in lists
            names.add(name);
            uuids.add(uuid);
            bools.add(true);
//            Query.whitelistImportInsert(name, uuid, true);
            System.out.println("Player: " + name + " " + uuid + " has been added to database");
            //getLogger().log(INFO, ChatColor.GREEN + "Player: " + name + " " + uuid + " has been added to database");
        }
        Query.whitelistImportBatchInsert(names, uuids, bools);
    }

    public static String readFileAsString(String path) {
        String str = null;
        try {
            str = new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
//            if (HeimdallPaper.debug) {
//                System.out.println("[Heimdall] DEBUG: error at FirstRunWhitelistParser.readFileAsString");
//            }
            e.printStackTrace();
        }
        return str;
    }
}
