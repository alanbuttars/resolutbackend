package com.alanbuttars.resolut.backend.serializers;

import com.alanbuttars.resolut.backend.Server;
import com.alanbuttars.resolut.backend.models.Account;
import com.alanbuttars.resolut.backend.models.Transfer;
import com.alanbuttars.resolut.backend.models.User;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class LinkSerializerTest {

    private final LinkSerializer serializer = new LinkSerializer();

    @Test
    public void testSerializeUser() {
        User user = new User("user-id", new ArrayList<>());
        JsonArray userJson = serializer.serialize(user);

        JsonObject selfLink = userJson.get(0).getAsJsonObject();
        Assert.assertEquals("self", selfLink.get("rel").getAsString());
        Assert.assertEquals("GET", selfLink.get("method").getAsString());
        Assert.assertEquals(Server.basename() + "/users/user-id", selfLink.get("href").getAsString());

        JsonObject getAccountsLink = userJson.get(1).getAsJsonObject();
        Assert.assertEquals("showAccounts", getAccountsLink.get("rel").getAsString());
        Assert.assertEquals("GET", getAccountsLink.get("method").getAsString());
        Assert.assertEquals(Server.basename() + "/users/user-id/accounts", getAccountsLink.get("href").getAsString());
    }

    @Test
    public void testSerializeAccount() {
        Account account = new Account("account-id", "user-id", 100);
        JsonArray accountJson = serializer.serialize(account);

        JsonObject selfLink = accountJson.get(0).getAsJsonObject();
        Assert.assertEquals("self", selfLink.get("rel").getAsString());
        Assert.assertEquals("GET", selfLink.get("method").getAsString());
        Assert.assertEquals(Server.basename() + "/users/user-id/accounts/account-id", selfLink.get("href").getAsString());

        JsonObject getUserLink = accountJson.get(1).getAsJsonObject();
        Assert.assertEquals("showUser", getUserLink.get("rel").getAsString());
        Assert.assertEquals("GET", getUserLink.get("method").getAsString());
        Assert.assertEquals(Server.basename() + "/users/user-id", getUserLink.get("href").getAsString());

        JsonObject getTransfersLink = accountJson.get(2).getAsJsonObject();
        Assert.assertEquals("showTransfers", getTransfersLink.get("rel").getAsString());
        Assert.assertEquals("GET", getTransfersLink.get("method").getAsString());
        Assert.assertEquals(Server.basename() + "/users/user-id/accounts/account-id/transfers", getTransfersLink.get("href").getAsString());

        JsonObject postTransferLink = accountJson.get(3).getAsJsonObject();
        Assert.assertEquals("createTransfer", postTransferLink.get("rel").getAsString());
        Assert.assertEquals("POST", postTransferLink.get("method").getAsString());
        Assert.assertEquals(Server.basename() + "/users/user-id/accounts/account-id/transfers", postTransferLink.get("href").getAsString());
    }

    @Test
    public void testSerializeTransfer() {
        Transfer transfer = new Transfer("sender-user-id", "sender-account-id", "receiver-user-id", "receiver-account-id", 100);
        JsonArray transferJson = serializer.serialize(transfer);

        JsonObject selfLink = transferJson.get(0).getAsJsonObject();
        Assert.assertEquals("self", selfLink.get("rel").getAsString());
        Assert.assertEquals("GET", selfLink.get("method").getAsString());
        Assert.assertEquals(Server.basename() + "/users/receiver-user-id/accounts/receiver-account-id/transfers/" + transfer.getId(), selfLink.get("href").getAsString());

        JsonObject getSenderUserLink = transferJson.get(1).getAsJsonObject();
        Assert.assertEquals("showSenderUser", getSenderUserLink.get("rel").getAsString());
        Assert.assertEquals("GET", getSenderUserLink.get("method").getAsString());
        Assert.assertEquals(Server.basename() + "/users/sender-user-id", getSenderUserLink.get("href").getAsString());

        JsonObject getSenderAccountLink = transferJson.get(2).getAsJsonObject();
        Assert.assertEquals("showSenderAccount", getSenderAccountLink.get("rel").getAsString());
        Assert.assertEquals("GET", getSenderAccountLink.get("method").getAsString());
        Assert.assertEquals(Server.basename() + "/users/sender-user-id/accounts/sender-account-id", getSenderAccountLink.get("href").getAsString());

        JsonObject getReceiverUserLink = transferJson.get(3).getAsJsonObject();
        Assert.assertEquals("showReceiverUser", getReceiverUserLink.get("rel").getAsString());
        Assert.assertEquals("GET", getReceiverUserLink.get("method").getAsString());
        Assert.assertEquals(Server.basename() + "/users/receiver-user-id", getReceiverUserLink.get("href").getAsString());

        JsonObject getReceiverAccountLink = transferJson.get(4).getAsJsonObject();
        Assert.assertEquals("showReceiverAccount", getReceiverAccountLink.get("rel").getAsString());
        Assert.assertEquals("GET", getReceiverAccountLink.get("method").getAsString());
        Assert.assertEquals(Server.basename() + "/users/receiver-user-id/accounts/receiver-account-id", getReceiverAccountLink.get("href").getAsString());
    }
}
