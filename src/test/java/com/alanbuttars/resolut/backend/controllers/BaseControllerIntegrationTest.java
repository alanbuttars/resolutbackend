package com.alanbuttars.resolut.backend.controllers;

import com.alanbuttars.resolut.backend.Server;
import com.alanbuttars.resolut.backend.Utils;
import com.alanbuttars.resolut.backend.services.SerializerService;
import com.google.gson.JsonElement;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class BaseControllerIntegrationTest {

    private static Server server = new Server();

    @BeforeClass
    public static void setup() {
        server.start();
    }

    @AfterClass
    public static void teardown() {
        server.stop();
    }

    public HttpURLConnection get(String path) throws IOException {
        URL url = new URL(Server.basename() + path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        return connection;
    }

    public HttpURLConnection post(String path, JsonElement jsonElement) throws IOException {
        URL url = new URL(Server.basename() + path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);
        try (OutputStream outputStream = connection.getOutputStream()) {
            byte[] input = SerializerService.getInstance().toJson(jsonElement).getBytes(Charset.defaultCharset());
            outputStream.write(input, 0, input.length);
        }

        return connection;
    }

    public String readResponse(HttpURLConnection connection) throws IOException {
        int statusCode = connection.getResponseCode();
        if (statusCode < HttpURLConnection.HTTP_BAD_REQUEST) {
            return Utils.readResponse(connection.getInputStream());
        } else if (statusCode == HttpURLConnection.HTTP_NOT_FOUND) {
            return null;
        } else {
            return Utils.readResponse(connection.getErrorStream());
        }
    }

    @Test
    public void testBadRoute() throws IOException {
        HttpURLConnection connection = get("");
        Assert.assertEquals(connection.getResponseCode(), 404);
    }
}
