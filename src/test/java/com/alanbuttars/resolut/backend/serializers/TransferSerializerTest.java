package com.alanbuttars.resolut.backend.serializers;

import com.alanbuttars.resolut.backend.models.Transfer;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.junit.Assert;
import org.junit.Test;

public class TransferSerializerTest {

    private final TransferSerializer serializer = new TransferSerializer();

    @Test
    public void testSerializeAndDeserializeObject() {
        Transfer serializedTransfer = new Transfer("sender-user-id", "sender-account-id", "receiver-user-id", "receiver-account-id", 100);
        JsonElement serialized = serializer.serialize(serializedTransfer, Transfer.class, null);
        Assert.assertNotNull(serialized.getAsJsonObject().getAsJsonArray("links"));

        Transfer deserializedTransfer = new Gson().fromJson(serialized, Transfer.class);
        Assert.assertNotNull(deserializedTransfer.getId());
        Assert.assertEquals("sender-user-id", deserializedTransfer.getSenderUserId());
        Assert.assertEquals("sender-account-id", deserializedTransfer.getSenderAccountId());
        Assert.assertEquals("receiver-user-id", deserializedTransfer.getReceiverUserId());
        Assert.assertEquals("receiver-account-id", deserializedTransfer.getReceiverAccountId());
        Assert.assertEquals(100, deserializedTransfer.getAmount());
        Assert.assertNotNull(deserializedTransfer.getCreatedAt());
    }
}
