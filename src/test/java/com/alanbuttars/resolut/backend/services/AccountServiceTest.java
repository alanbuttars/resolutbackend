package com.alanbuttars.resolut.backend.services;

import com.alanbuttars.resolut.backend.models.Account;
import com.alanbuttars.resolut.backend.models.Result;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class AccountServiceTest {

    @Test
    public void testListAccounts_ValidUserId() throws IOException {
        Result<List<Account>> result = AccountService.getInstance().listAccounts("alice");
        Assert.assertTrue(result.succeeded());
        List<Account> accounts = result.getTarget();
        Assert.assertEquals(2, accounts.size());
        Assert.assertEquals("home", accounts.get(0).getId());
        Assert.assertEquals("work", accounts.get(1).getId());
    }

    @Test
    public void testListAccounts_InvalidUserId() throws IOException {
        Result<List<Account>> result = AccountService.getInstance().listAccounts("blah");
        Assert.assertFalse(result.succeeded());
        Assert.assertEquals("User blah could not be found", result.getErrorMessage());
    }

    @Test
    public void testGetAccount_ValidAccountId() throws IOException {
        Result<Account> result = AccountService.getInstance().getAccount("alice", "home");
        Assert.assertTrue(result.succeeded());
        Account account = result.getTarget();
        Assert.assertEquals("home", account.getId());
        Assert.assertEquals("alice", account.getUserId());
    }

    @Test
    public void testGetAccount_InvalidUserId() throws IOException {
        Result<Account> result = AccountService.getInstance().getAccount("blah", "home");
        Assert.assertFalse(result.succeeded());
        Assert.assertEquals("User blah could not be found", result.getErrorMessage());
    }

    @Test
    public void testGetAccount_InvalidAccountId() throws IOException {
        Result<Account> result = AccountService.getInstance().getAccount("alice", "blah");
        Assert.assertFalse(result.succeeded());
        Assert.assertEquals("Account blah for user alice could not be found", result.getErrorMessage());
    }
}
