package com.alanbuttars.resolut.backend.serializers;

import com.alanbuttars.resolut.backend.models.Account;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.junit.Assert;
import org.junit.Test;

public class AccountSerializerTest {

    private final AccountSerializer serializer = new AccountSerializer();

    @Test
    public void testSerializeAndDeserializeObject() {
        Account serializedAccount = new Account("account-id", "user-id", 100);
        JsonElement serialized = serializer.serialize(serializedAccount, Account.class, null);
        Assert.assertNotNull(serialized.getAsJsonObject().getAsJsonArray("links"));

        Account deserializedUser = new Gson().fromJson(serialized, Account.class);
        Assert.assertEquals("account-id", deserializedUser.getId());
        Assert.assertEquals("user-id", deserializedUser.getUserId());
        Assert.assertEquals(100, deserializedUser.getBalance());
    }
}
