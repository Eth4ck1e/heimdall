package com.bifrostsmp.heimdall.mojangAPI;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NameToID {

    private static HttpResponse<String> response;

    public static String nameToID(String name) {
        // START Mojang api - to get UUID from Username
        String GET_URL = "https://api.mojang.com/users/profiles/minecraft/";
        Date date = new Date();
        Timestamp tsd = new Timestamp(date.getTime());
        String ts = new SimpleDateFormat("HH:mm:ss").format(tsd);
        String id;
        HttpClient client = HttpClient.newHttpClient(); // create get client
        HttpRequest request =
                HttpRequest.newBuilder().uri(URI.create(GET_URL + name + "?at=" + ts)).build(); // build uri
        try {
            response =
                    client.send(request, HttpResponse.BodyHandlers.ofString()); // store response in variable
        } catch (IOException | InterruptedException e) {
            return null;
        }
        try {
            String json = response.body(); // convert response to string
            JSONObject obj = (JSONObject) JSONValue.parse(json); // parse string
            id =
                    addChar(
                            (String)
                                    obj.get(
                                            "id")); // gets id from get request and sends to addChar to correct the format
            // for whitelist.json
        } catch (NullPointerException e) {
            return null;
        }

        // END Mojang api
        return id;
    }

    private static String addChar(String str) {
        char ch = '-';
        int position1 = 8;
        int position2 = 13;
        int position3 = 18;
        int position4 = 23;

        String newString = str.substring(0, position1) + ch + str.substring(position1);
        newString = newString.substring(0, position2) + ch + newString.substring(position2);
        newString = newString.substring(0, position3) + ch + newString.substring(position3);
        newString = newString.substring(0, position4) + ch + newString.substring(position4);

        return newString;
    }
}
