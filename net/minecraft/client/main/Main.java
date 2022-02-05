package net.minecraft.client.main;

import me.errordev.parser.StringParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main {
    public static void main(String[] args) throws IOException {
        final StringParser parser = new StringParser(args);
        HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:5500/auth").openConnection();
        connection.setRequestProperty("username", parser.get("username").getAsString());
        connection.setRequestProperty("uuid", parser.get("uuid").getAsString());
        connection.setRequestProperty("accessToken", parser.get("accessToken").getAsString());
        connection.connect();
        System.out.println(connection.getResponseMessage() + " | " + connection.getResponseCode());
    }
}