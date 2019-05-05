package com.alanbuttars.resolut.backend.models;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class UserTest {

    @Test
    public void testConstructor() {
        User user = new User("user-id", Arrays.asList(
                new Account("account-id-1", "user-id", 100),//
                new Account("account-id-2", "user-id", 200)
        ));

        Assert.assertEquals("user-id", user.getId());
        Assert.assertEquals(2, user.getAccounts().size());
        Assert.assertNotNull(user.getAccounts().get("account-id-1"));
        Assert.assertNotNull(user.getAccounts().get("account-id-2"));
    }
}
