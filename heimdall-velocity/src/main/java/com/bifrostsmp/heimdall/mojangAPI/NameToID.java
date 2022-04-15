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

    HttpClient client = HttpClient.newHttpClient(); // create get client
    HttpRequest request =
        HttpRequest.newBuilder().uri(URI.create(GET_URL + name + "?at=" + ts)).build(); // build uri
    try {
      response =
          client.send(request, HttpResponse.BodyHandlers.ofString()); // store response in variable
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }

    String json = response.body(); // convert response to string
    JSONObject obj = (JSONObject) JSONValue.parse(json); // parse string
    String id = (String) obj.get("id"); // get id
    // END Mojang api
    return id;
  }
}
