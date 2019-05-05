package com.alanbuttars.resolut.backend.serializers;

import com.alanbuttars.resolut.backend.models.Account;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class AccountSerializerTest {

    private final AccountSerializer serializer = new AccountSerializer();

    @Test
    public void testSerializeAndDeserializeObject() {
        Account serializedAccount = new Account("account-id", "user-id", 100);
        JsonElement serialized = serializer.serialize(serializedAccount, Account.class, null);
        Account deserializedAccount = serializer.deserialize(serialized, Account.class, null);

        Assert.assertEquals("account-id", deserializedAccount.getId());
        Assert.assertEquals("user-id", deserializedAccount.getUserId());
        Assert.assertEquals(100, deserializedAccount.getBalance());
    }

    @Test
    public void testSerializeAndDeserializeArray() {
        List<Account> serializedAccounts = Arrays.asList(
                new Account("account-id-1", "user-id-1", 100),//
                new Account("account-id-2", "user-id-2", 200)
        );
        JsonArray serialized = serializer.serialize(serializedAccounts, null);
        List<Account> deserializedAccounts = serializer.deserialize(serialized, null);

        Assert.assertEquals("account-id-1", deserializedAccounts.get(0).getId());
        Assert.assertEquals("user-id-1", deserializedAccounts.get(0).getUserId());
        Assert.assertEquals(100, deserializedAccounts.get(0).getBalance());
        Assert.assertEquals("account-id-2", deserializedAccounts.get(1).getId());
        Assert.assertEquals("user-id-2", deserializedAccounts.get(1).getUserId());
        Assert.assertEquals(200, deserializedAccounts.get(1).getBalance());
    }
}
