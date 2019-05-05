package com.alanbuttars.resolut.backend.services;

import com.alanbuttars.resolut.backend.models.Account;
import com.alanbuttars.resolut.backend.models.Result;
import com.alanbuttars.resolut.backend.models.Transfer;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class TransferServiceTest {
    @Test
    public void testListTransfers_ValidTransferId() throws IOException {
        int transferId1 = TransferService.getInstance().createTransfer("alice", "home", "eve", "work", 100).getTarget().getId();
        int transferId2 = TransferService.getInstance().createTransfer("eve", "work", "alice", "home", 100).getTarget().getId();

        Result<List<Transfer>> result = TransferService.getInstance().listTransfers("alice", "home");
        Assert.assertTrue(result.succeeded());
        List<Transfer> transfers = result.getTarget();
        System.out.println(new Gson().toJson(transfers));
        Assert.assertEquals(transferId1, transfers.get(transfers.size() - 2).getId());
        Assert.assertEquals(transferId2, transfers.get(transfers.size() - 1).getId());
    }

    @Test
    public void testListTransfers_InvalidUserId() throws IOException {
        Result<List<Transfer>> result = TransferService.getInstance().listTransfers("blah", "home");
        Assert.assertFalse(result.succeeded());
        Assert.assertEquals("User blah could not be found", result.getErrorMessage());
    }

    @Test
    public void testListTransfers_InvalidAccountId() throws IOException {
        Result<List<Transfer>> result = TransferService.getInstance().listTransfers("alice", "blah");
        Assert.assertFalse(result.succeeded());
        Assert.assertEquals("Account blah for user alice could not be found", result.getErrorMessage());
    }

    @Test
    public void testGetTransfer_ValidTransferId() throws IOException {
        int transferId = TransferService.getInstance().createTransfer("alice", "home", "bob", "home", 100).getTarget().getId();

        Result<Transfer> result = TransferService.getInstance().getTransfer("alice", "home", "" + transferId);
        Assert.assertTrue(result.succeeded());

        Transfer transfer = result.getTarget();
        Assert.assertEquals(transferId, transfer.getId());
        Assert.assertEquals("alice", transfer.getSenderUserId());
        Assert.assertEquals("home", transfer.getSenderAccountId());
        Assert.assertEquals("bob", transfer.getReceiverUserId());
        Assert.assertEquals("home", transfer.getReceiverAccountId());
        Assert.assertEquals(100, transfer.getAmount());
    }

    @Test
    public void testGetTransfer_InvalidUserId() throws IOException {
        Result<Transfer> result = TransferService.getInstance().getTransfer("blah", "home", "1");
        Assert.assertFalse(result.succeeded());
        Assert.assertEquals("User blah could not be found", result.getErrorMessage());
    }

    @Test
    public void testGetTransfer_InvalidAccountId() throws IOException {
        Result<Transfer> result = TransferService.getInstance().getTransfer("alice", "blah", "1");
        Assert.assertFalse(result.succeeded());
        Assert.assertEquals("Account blah for user alice could not be found", result.getErrorMessage());
    }

    @Test
    public void testGetTransfer_InvalidTransferId() throws IOException {
        Result<Transfer> result = TransferService.getInstance().getTransfer("alice", "home", "0");
        Assert.assertFalse(result.succeeded());
        Assert.assertEquals("Transfer 0 for user alice and account home could not be found", result.getErrorMessage());
    }

    @Test
    public void testCreateTransfer() throws IOException {
        Account senderAccount = AccountService.getInstance().getAccount("alice", "home").getTarget();
        Account receiverAccount = AccountService.getInstance().getAccount("eve", "work").getTarget();

        int senderAccountPreviousBalance = senderAccount.getBalance();
        int receiverAccountPreviousBalance = receiverAccount.getBalance();

        Result<Transfer> result = TransferService.getInstance().createTransfer("alice", "home", "eve", "work", 100);
        Assert.assertTrue(result.succeeded());
        Transfer transfer = result.getTarget();
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

        Result<Transfer> result = TransferService.getInstance().createTransfer("alice", "home", "alice", "work", 100);
        Assert.assertTrue(result.succeeded());
        Transfer transfer = result.getTarget();
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
        Result<Transfer> result = TransferService.getInstance().createTransfer("alice", "home", "eve", "work", 0);
        Assert.assertFalse(result.succeeded());
        Assert.assertEquals("Amount must be greater than 0", result.getErrorMessage());
    }

    @Test
    public void testCreateTransfer_ExcessiveAmount() throws IOException {
        Result<Transfer> result = TransferService.getInstance().createTransfer("alice", "home", "eve", "work", 99999);
        Assert.assertFalse(result.succeeded());
        Assert.assertEquals("Account home for user alice has insufficient funds", result.getErrorMessage());
    }

    @Test
    public void testCreateTransfer_InvalidSenderUserId() throws IOException {
        Result<Transfer> result = TransferService.getInstance().createTransfer("blah", "home", "eve", "work", 100);
        Assert.assertFalse(result.succeeded());
        Assert.assertEquals("User blah could not be found", result.getErrorMessage());
    }

    @Test
    public void testCreateTransfer_InvalidSenderAccountId() throws IOException {
        Result<Transfer> result = TransferService.getInstance().createTransfer("alice", "blah", "eve", "work", 100);
        Assert.assertFalse(result.succeeded());
        Assert.assertEquals("Account blah for user alice could not be found", result.getErrorMessage());
    }

    @Test
    public void testCreateTransfer_InvalidReceiverUserId() throws IOException {
        Result<Transfer> result = TransferService.getInstance().createTransfer("alice", "home", "blah", "work", 100);
        Assert.assertFalse(result.succeeded());
        Assert.assertEquals("User blah could not be found", result.getErrorMessage());
    }

    @Test
    public void testCreateTransfer_InvalidReceiverAccountId() throws IOException {
        Result<Transfer> result = TransferService.getInstance().createTransfer("alice", "home", "eve", "blah", 100);
        Assert.assertFalse(result.succeeded());
        Assert.assertEquals("Account blah for user eve could not be found", result.getErrorMessage());
    }
}
