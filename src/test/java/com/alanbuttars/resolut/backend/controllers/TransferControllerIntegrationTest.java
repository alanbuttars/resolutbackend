package com.alanbuttars.resolut.backend.controllers;

import com.alanbuttars.resolut.backend.models.Account;
import com.alanbuttars.resolut.backend.models.Error;
import com.alanbuttars.resolut.backend.models.Transfer;
import com.alanbuttars.resolut.backend.services.AccountService;
import com.alanbuttars.resolut.backend.services.SerializerService;
import com.alanbuttars.resolut.backend.services.TransferService;
import com.google.gson.JsonObject;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;

public class TransferControllerIntegrationTest extends BaseControllerIntegrationTest {

    @Test
    public void testListTransfers_ValidTransferId() throws IOException {
        int transferId1 = TransferService.getInstance().createTransfer("alice", "home", "eve", "work", 100).getTarget().getId();
        int transferId2 = TransferService.getInstance().createTransfer("eve", "work", "alice", "home", 100).getTarget().getId();

        HttpURLConnection connection = get("/users/alice/accounts/home/transfers");
        Assert.assertEquals(200, connection.getResponseCode());
        String body = readResponse(connection);
        Transfer[] transfers = SerializerService.getInstance().fromJson(body, Transfer[].class);
        Assert.assertEquals(transferId1, transfers[0].getId());
        Assert.assertEquals(transferId2, transfers[1].getId());
    }

    @Test
    public void testListTransfers_InvalidUserId() throws IOException {
        HttpURLConnection connection = get("/users/blah/accounts/home/transfers");
        Assert.assertEquals(400, connection.getResponseCode());
        String body = readResponse(connection);
        Error error = SerializerService.getInstance().fromJson(body, Error.class);
        Assert.assertEquals("User blah could not be found", error.getErrorMessage());
    }

    @Test
    public void testListTransfers_InvalidAccountId() throws IOException {
        HttpURLConnection connection = get("/users/alice/accounts/blah/transfers");
        Assert.assertEquals(400, connection.getResponseCode());
        String body = readResponse(connection);
        Error error = SerializerService.getInstance().fromJson(body, Error.class);
        Assert.assertEquals("Account blah for user alice could not be found", error.getErrorMessage());
    }

    @Test
    public void testGetTransfer_ValidTransferId() throws IOException {
        int transferId = TransferService.getInstance().createTransfer("alice", "home", "bob", "home", 100).getTarget().getId();

        HttpURLConnection connection = get("/users/alice/accounts/home/transfers/" + transferId);
        Assert.assertEquals(200, connection.getResponseCode());

        String body = readResponse(connection);
        Transfer transfer = SerializerService.getInstance().fromJson(body, Transfer.class);
        Assert.assertEquals(transferId, transfer.getId());
        Assert.assertEquals("alice", transfer.getSenderUserId());
        Assert.assertEquals("home", transfer.getSenderAccountId());
        Assert.assertEquals("bob", transfer.getReceiverUserId());
        Assert.assertEquals("home", transfer.getReceiverAccountId());
        Assert.assertEquals(100, transfer.getAmount());
    }

    @Test
    public void testGetTransfer_InvalidUserId() throws IOException {
        HttpURLConnection connection = get("/users/blah/accounts/home/transfers/1");
        Assert.assertEquals(400, connection.getResponseCode());
        String body = readResponse(connection);
        Error error = SerializerService.getInstance().fromJson(body, Error.class);
        Assert.assertEquals("User blah could not be found", error.getErrorMessage());
    }

    @Test
    public void testGetTransfer_InvalidAccountId() throws IOException {
        HttpURLConnection connection = get("/users/alice/accounts/blah/transfers/1");
        Assert.assertEquals(400, connection.getResponseCode());
        String body = readResponse(connection);
        Error error = SerializerService.getInstance().fromJson(body, Error.class);
        Assert.assertEquals("Account blah for user alice could not be found", error.getErrorMessage());
    }

    @Test
    public void testGetTransfer_InvalidTransferId() throws IOException {
        HttpURLConnection connection = get("/users/alice/accounts/home/transfers/0");
        Assert.assertEquals(400, connection.getResponseCode());
        String body = readResponse(connection);
        Error error = SerializerService.getInstance().fromJson(body, Error.class);
        Assert.assertEquals("Transfer 0 for user alice and account home could not be found", error.getErrorMessage());
    }

    @Test
    public void testGetTransfer_NonNumericTransferId() throws IOException {
        HttpURLConnection connection = get("/users/alice/accounts/home/transfers/blah");
        Assert.assertEquals(400, connection.getResponseCode());
        String body = readResponse(connection);
        Error error = SerializerService.getInstance().fromJson(body, Error.class);
        Assert.assertEquals("Transfer ID must be a number", error.getErrorMessage());
    }

    @Test
    public void testCreateTransfer() throws IOException {
        Account senderAccount = AccountService.getInstance().getAccount("alice", "home").getTarget();
        Account receiverAccount = AccountService.getInstance().getAccount("eve", "work").getTarget();

        int senderAccountPreviousBalance = senderAccount.getBalance();
        int receiverAccountPreviousBalance = receiverAccount.getBalance();

        JsonObject json = new JsonObject();
        json.addProperty("receiverUserId", "eve");
        json.addProperty("receiverAccountId", "work");
        json.addProperty("amount", 100);

        HttpURLConnection connection = post("/users/alice/accounts/home/transfers", json);
        Assert.assertEquals(200, connection.getResponseCode());
        String body = readResponse(connection);
        Transfer transfer = SerializerService.getInstance().fromJson(body, Transfer.class);
        Assert.assertNotNull(transfer.getId());
        Assert.assertEquals("alice", transfer.getSenderUserId());
        Assert.assertEquals("home", transfer.getSenderAccountId());
        Assert.assertEquals("eve", transfer.getReceiverUserId());
        Assert.assertEquals("work", transfer.getReceiverAccountId());
        Assert.assertEquals(100, transfer.getAmount());
        Assert.assertNotNull(transfer.getCreatedAt());

        Assert.assertEquals(senderAccountPreviousBalance - 100, senderAccount.getBalance());
        Assert.assertEquals(receiverAccountPreviousBalance + 100, receiverAccount.getBalance());
    }

    @Test
    public void testCreateTransfer_ToOneself() throws IOException {
        Account senderAccount = AccountService.getInstance().getAccount("alice", "home").getTarget();
        Account receiverAccount = AccountService.getInstance().getAccount("alice", "work").getTarget();

        int senderAccountPreviousBalance = senderAccount.getBalance();
        int receiverAccountPreviousBalance = receiverAccount.getBalance();

        JsonObject json = new JsonObject();
        json.addProperty("receiverUserId", "alice");
        json.addProperty("receiverAccountId", "work");
        json.addProperty("amount", 100);

        HttpURLConnection connection = post("/users/alice/accounts/home/transfers", json);
        Assert.assertEquals(200, connection.getResponseCode());
        String body = readResponse(connection);
        Transfer transfer = SerializerService.getInstance().fromJson(body, Transfer.class);
        Assert.assertNotNull(transfer.getId());
        Assert.assertEquals("alice", transfer.getSenderUserId());
        Assert.assertEquals("home", transfer.getSenderAccountId());
        Assert.assertEquals("alice", transfer.getReceiverUserId());
        Assert.assertEquals("work", transfer.getReceiverAccountId());
        Assert.assertEquals(100, transfer.getAmount());
        Assert.assertNotNull(transfer.getCreatedAt());

        Assert.assertEquals(senderAccountPreviousBalance - 100, senderAccount.getBalance());
        Assert.assertEquals(receiverAccountPreviousBalance + 100, receiverAccount.getBalance());
    }

    @Test
    public void testCreateTransfer_InvalidAmount() throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("receiverUserId", "eve");
        json.addProperty("receiverAccountId", "work");
        json.addProperty("amount", 0);

        HttpURLConnection connection = post("/users/alice/accounts/home/transfers", json);
        Assert.assertEquals(400, connection.getResponseCode());
        String body = readResponse(connection);
        Error error = SerializerService.getInstance().fromJson(body, Error.class);
        Assert.assertEquals("Amount must be greater than 0", error.getErrorMessage());
    }

    @Test
    public void testCreateTransfer_ExcessiveAmount() throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("receiverUserId", "eve");
        json.addProperty("receiverAccountId", "work");
        json.addProperty("amount", 99999);

        HttpURLConnection connection = post("/users/alice/accounts/home/transfers", json);
        Assert.assertEquals(400, connection.getResponseCode());
        String body = readResponse(connection);
        Error error = SerializerService.getInstance().fromJson(body, Error.class);
        Assert.assertEquals("Account home for user alice has insufficient funds", error.getErrorMessage());
    }

    @Test
    public void testCreateTransfer_InvalidSenderUserId() throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("receiverUserId", "eve");
        json.addProperty("receiverAccountId", "work");
        json.addProperty("amount", 100);

        HttpURLConnection connection = post("/users/blah/accounts/home/transfers", json);
        Assert.assertEquals(400, connection.getResponseCode());
        String body = readResponse(connection);
        Error error = SerializerService.getInstance().fromJson(body, Error.class);
        Assert.assertEquals("User blah could not be found", error.getErrorMessage());
    }

    @Test
    public void testCreateTransfer_InvalidSenderAccountId() throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("receiverUserId", "eve");
        json.addProperty("receiverAccountId", "work");
        json.addProperty("amount", 100);

        HttpURLConnection connection = post("/users/alice/accounts/blah/transfers", json);
        Assert.assertEquals(400, connection.getResponseCode());
        String body = readResponse(connection);
        Error error = SerializerService.getInstance().fromJson(body, Error.class);
        Assert.assertEquals("Account blah for user alice could not be found", error.getErrorMessage());
    }

    @Test
    public void testCreateTransfer_InvalidReceiverUserId() throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("receiverUserId", "blah");
        json.addProperty("receiverAccountId", "work");
        json.addProperty("amount", 100);

        HttpURLConnection connection = post("/users/alice/accounts/home/transfers", json);
        Assert.assertEquals(400, connection.getResponseCode());
        String body = readResponse(connection);
        Error error = SerializerService.getInstance().fromJson(body, Error.class);
        Assert.assertEquals("User blah could not be found", error.getErrorMessage());
    }

    @Test
    public void testCreateTransfer_InvalidReceiverAccountId() throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("receiverUserId", "eve");
        json.addProperty("receiverAccountId", "blah");
        json.addProperty("amount", 100);

        HttpURLConnection connection = post("/users/alice/accounts/home/transfers", json);
        Assert.assertEquals(400, connection.getResponseCode());
        String body = readResponse(connection);
        Error error = SerializerService.getInstance().fromJson(body, Error.class);
        Assert.assertEquals("Account blah for user eve could not be found", error.getErrorMessage());
    }
}
