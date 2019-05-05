package com.alanbuttars.resolut.backend.models;

import org.junit.Assert;
import org.junit.Test;

public class TransferTest {

    @Test
    public void testConstructor() {
        Transfer transfer = new Transfer("sender-user-id", "sender-account-id", "receiver-user-id", "receiver-account-id", 100);
        Assert.assertNotNull(transfer.getId());
        Assert.assertEquals("sender-user-id", transfer.getSenderUserId());
        Assert.assertEquals("sender-account-id", transfer.getSenderAccountId());
        Assert.assertEquals("receiver-user-id", transfer.getReceiverUserId());
        Assert.assertEquals("receiver-account-id", transfer.getReceiverAccountId());
        Assert.assertEquals(100, transfer.getAmount());
        Assert.assertNotNull(transfer.getCreatedAt());
    }
}
