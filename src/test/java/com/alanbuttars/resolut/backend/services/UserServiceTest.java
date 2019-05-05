package com.alanbuttars.resolut.backend.services;

import com.alanbuttars.resolut.backend.models.Result;
import com.alanbuttars.resolut.backend.models.User;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class UserServiceTest {

    @Test
    public void testListUsers() throws IOException {
        Result<List<User>> result = UserService.getInstance().listUsers();
        Assert.assertTrue(result.succeeded());

        List<User> users = result.getTarget();
        Assert.assertEquals(3, users.size());
        Assert.assertEquals("alice", users.get(0).getId());
        Assert.assertEquals("bob", users.get(1).getId());
        Assert.assertEquals("eve", users.get(2).getId());
    }

    @Test
    public void testGetUser_ValidUserId() throws IOException {
        Result<User> result = UserService.getInstance().getUser("alice");
        Assert.assertTrue(result.succeeded());

        User user = result.getTarget();
        Assert.assertEquals("alice", user.getId());
        Assert.assertEquals(2, user.getAccounts().size());
        Assert.assertEquals("home", user.getAccounts().get("home").getId());
        Assert.assertEquals("work", user.getAccounts().get("work").getId());
    }

    @Test
    public void testGetUser_InvalidUserId() throws IOException {
        Result<User> result = UserService.getInstance().getUser("blah");
        Assert.assertFalse(result.succeeded());
        Assert.assertEquals("User blah could not be found", result.getErrorMessage());
    }
}
