package com.alanbuttars.resolut.backend.serializers;

import com.alanbuttars.resolut.backend.models.Account;
import com.alanbuttars.resolut.backend.models.User;
import com.google.gson.JsonElement;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class UserSerializerTest {

    private final UserSerializer serializer = new UserSerializer();

    @Test
    public void testSerializeAndDeserializeObject() {
        List<Account> serializedAccounts = Arrays.asList(
                new Account("account-id-1", "user-id-1", 100),//
                new Account("account-id-2", "user-id-1", 200)
        );
        User serializedUser = new User("user-id", serializedAccounts);
        JsonElement serialized = serializer.serialize(serializedUser, User.class, null);
        User deserializedUser = serializer.deserialize(serialized, User.class, null);
        Assert.assertEquals("user-id", deserializedUser.getId());

        Map<String, Account> deserializedAccounts = deserializedUser.getAccounts();
        Assert.assertEquals("account-id-1", deserializedAccounts.get("account-id-1").getId());
        Assert.assertEquals("user-id-1", deserializedAccounts.get("account-id-1").getUserId());
        Assert.assertEquals(100, deserializedAccounts.get("account-id-1").getBalance());
        Assert.assertEquals("account-id-2", deserializedAccounts.get("account-id-2").getId());
        Assert.assertEquals("user-id-1", deserializedAccounts.get("account-id-2").getUserId());
        Assert.assertEquals(200, deserializedAccounts.get("account-id-2").getBalance());
    }
}
