package com.alanbuttars.resolut.backend.controllers;

import com.alanbuttars.resolut.backend.models.Account;
import com.alanbuttars.resolut.backend.models.Error;
import com.alanbuttars.resolut.backend.services.SerializerService;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;

public class AccountControllerIntegrationTest extends BaseControllerIntegrationTest {

    @Test
    public void testListAccounts_ValidUserId() throws IOException {
        HttpURLConnection connection = get("/users/alice/accounts");
        Assert.assertEquals(200, connection.getResponseCode());

        String body = readResponse(connection);
        Account[] accounts = SerializerService.getInstance().fromJson(body, Account[].class);
        Assert.assertEquals(2, accounts.length);
        Assert.assertEquals("home", accounts[0].getId());
        Assert.assertEquals("work", accounts[1].getId());
    }

    @Test
    public void testListAccounts_InvalidUserId() throws IOException {
        HttpURLConnection connection = get("/users/blah/accounts");
        Assert.assertEquals(400, connection.getResponseCode());
        String body = readResponse(connection);
        Error error = SerializerService.getInstance().fromJson(body, Error.class);
        Assert.assertEquals("User blah could not be found", error.getErrorMessage());
    }

    @Test
    public void testGetAccount_ValidAccountId() throws IOException {
        HttpURLConnection connection = get("/users/alice/accounts/home");
        Assert.assertEquals(200, connection.getResponseCode());

        String body = readResponse(connection);
        Account account = SerializerService.getInstance().fromJson(body, Account.class);
        Assert.assertEquals("home", account.getId());
        Assert.assertEquals("alice", account.getUserId());
    }

    @Test
    public void testGetAccount_InvalidUserId() throws IOException {
        HttpURLConnection connection = get("/users/blah/accounts/work");
        Assert.assertEquals(400, connection.getResponseCode());
        String body = readResponse(connection);
        Error error = SerializerService.getInstance().fromJson(body, Error.class);
        Assert.assertEquals("User blah could not be found", error.getErrorMessage());
    }

    @Test
    public void testGetAccount_InvalidAccountId() throws IOException {
        HttpURLConnection connection = get("/users/alice/accounts/blah");
        Assert.assertEquals(400, connection.getResponseCode());
        String body = readResponse(connection);
        Error error = SerializerService.getInstance().fromJson(body, Error.class);
        Assert.assertEquals("Account blah for user alice could not be found", error.getErrorMessage());
    }
}
