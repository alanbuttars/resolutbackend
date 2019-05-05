package com.alanbuttars.resolut.backend.controllers;

import com.alanbuttars.resolut.backend.models.Error;
import com.alanbuttars.resolut.backend.models.User;
import com.alanbuttars.resolut.backend.services.SerializerService;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;

public class UserControllerIntegrationTest extends BaseControllerIntegrationTest {

    @Test
    public void testListUsers() throws IOException {
        HttpURLConnection connection = get("/users");
        Assert.assertEquals(200, connection.getResponseCode());

        String body = readResponse(connection);
        User[] users = SerializerService.getInstance().fromJson(body, User[].class);
        Assert.assertEquals(3, users.length);
        Assert.assertEquals("alice", users[0].getId());
        Assert.assertEquals("bob", users[1].getId());
        Assert.assertEquals("eve", users[2].getId());
    }

    @Test
    public void testGetUser_ValidUserId() throws IOException {
        HttpURLConnection connection = get("/users/alice");
        Assert.assertEquals(200, connection.getResponseCode());

        String body = readResponse(connection);
        User user = SerializerService.getInstance().fromJson(body, User.class);
        Assert.assertEquals("alice", user.getId());
        Assert.assertEquals(2, user.getAccounts().size());
        Assert.assertEquals("home", user.getAccounts().get("home").getId());
        Assert.assertEquals("work", user.getAccounts().get("work").getId());
    }

    @Test
    public void testGetUser_InvalidUserId() throws IOException {
        HttpURLConnection connection = get("/users/blah");
        Assert.assertEquals(400, connection.getResponseCode());
        String body = readResponse(connection);
        Error error = SerializerService.getInstance().fromJson(body, Error.class);
        Assert.assertEquals("User blah could not be found", error.getErrorMessage());
    }
}
