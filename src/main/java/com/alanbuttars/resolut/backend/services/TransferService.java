package com.alanbuttars.resolut.backend.services;

import com.alanbuttars.resolut.backend.models.Account;
import com.alanbuttars.resolut.backend.models.Result;
import com.alanbuttars.resolut.backend.models.Transfer;

import java.util.*;

/**
 * Service for {@link Transfer} actions.
 */
public class TransferService {

    private static final TransferService INSTANCE = new TransferService();
    private final Map<String, Map<String, Map<Integer, Transfer>>> transfersBySenderUserId;
    private final Map<String, Map<String, Map<Integer, Transfer>>> transfersByReceiverUserId;

    /**
     * Initializes the data storage of {@link Transfer}s for each existing {@link com.alanbuttars.resolut.backend.models.User} registered in {@link UserService}.
     */
    private TransferService() {
        /**
         * Using HashMaps here for performance reasons:
         *      - There's no need to maintain sorting or insertion order, so TreeMap and LinkedHashMap are unnecessary
         *      - Transfers are frequent operations; we will benefit from the O(1) access time
         */
        this.transfersBySenderUserId = new HashMap<>();
        this.transfersByReceiverUserId = new HashMap<>();

        UserService.getInstance().listUsers().getTarget().forEach(user -> {
            /**
             * Using HashMaps here for the same reasons as above
             */
            Map<String, Map<Integer, Transfer>> transfersBySenderAccountId = new HashMap<>();
            Map<String, Map<Integer, Transfer>> transfersByReceiverAccountId = new HashMap<>();

            user.getAccounts().values().forEach(account -> {
                /**
                 * Using LinkedHashMaps here for performance reasons:
                 *      - We want to maintain ordering based on the key (which is the transfer ID), but those IDs will be inserted in-order anyway
                 *      - We will get both ordering and O(1) access time
                 */
                transfersBySenderAccountId.put(account.getId(), new LinkedHashMap<>());
                transfersByReceiverAccountId.put(account.getId(), new LinkedHashMap<>());
            });

            this.transfersBySenderUserId.put(user.getId(), transfersBySenderAccountId);
            this.transfersByReceiverUserId.put(user.getId(), transfersByReceiverAccountId);
        });
    }

    /**
     * Returns the singleton instance of this class.
     */
    public static TransferService getInstance() {
        return INSTANCE;
    }

    /**
     * Returns the <code>List</code> of {@link Transfer}s associated with a given <code>userId</code> and <code>accountId</code>, if they exist.
     */
    public Result<List<Transfer>> listTransfers(String userId, String accountId) {
        /* 1. Fetch the account */
        Result<Account> accountResult = AccountService.getInstance().getAccount(userId, accountId);
        if (!accountResult.succeeded()) {
            return Result.failure(accountResult.getErrorMessage());
        }
        Account account = accountResult.getTarget();

        List<Transfer> transfers = new LinkedList<>();
        /* 2. Fetch the transfers sent by that account */
        transfers.addAll(listTransfers(account, transfersBySenderUserId));
        /* 3. Fetch the transfers received by that account */
        transfers.addAll(listTransfers(account, transfersByReceiverUserId));
        /* 4. Sort to ensure API predictability */
        Collections.sort(transfers);
        return Result.success(transfers);
    }

    /**
     * Given an account and a mapping of transfers owned by a user, returns the transfers owned by that account.
     */
    private List<Transfer> listTransfers(Account account, Map<String, Map<String, Map<Integer, Transfer>>> transfersByUserId) {
        Map<String, Map<Integer, Transfer>> transfersByAccountId = transfersByUserId.get(account.getUserId());
        Map<Integer, Transfer> transfersById = transfersByAccountId.get(account.getId());
        return new ArrayList<>(transfersById.values());
    }

    /**
     * Returns the {@link Transfer} associated with a given <code>userId</code>, <code>accountId</code>, and <code>transferId</code>.
     */
    public Result<Transfer> getTransfer(String userId, String accountId, String transferIdString) {
        /* 1. Check the transferId */
        int transferId = 0;
        try {
            transferId = Integer.parseInt(transferIdString);
        } catch (NumberFormatException e) {
            return Result.failure("Transfer ID must be a number");
        }

        /* 2. Fetch the account */
        Result<Account> accountResult = AccountService.getInstance().getAccount(userId, accountId);
        if (!accountResult.succeeded()) {
            return Result.failure(accountResult.getErrorMessage());
        }
        Account account = accountResult.getTarget();

        /* 3. Check to see if the account received the given transaction; if so, return it */
        Map<String, Map<Integer, Transfer>> receivedTransfersByAccountId = transfersByReceiverUserId.get(account.getUserId());
        Map<Integer, Transfer> receiverTransfersById = receivedTransfersByAccountId.get(accountId);
        Transfer receivedTransfer = receiverTransfersById.get(transferId);
        if (receivedTransfer != null) {
            return Result.success(receivedTransfer);
        }

        /* 4. Check to see if the account sent the given transaction; if so, return it */
        Map<String, Map<Integer, Transfer>> sentTransfersByAccountId = transfersBySenderUserId.get(account.getUserId());
        Map<Integer, Transfer> sentTransfersById = sentTransfersByAccountId.get(accountId);
        Transfer sentTransfer = sentTransfersById.get(transferId);
        if (sentTransfer != null) {
            return Result.success(sentTransfer);
        }

        /* 5. Return a failure message if the account is not associated with transaction */
        return Result.failure("Transfer " + transferId + " for user " + userId + " and account " + accountId + " could not be found");
    }

    /**
     * Creates a transaction with the given params.
     */
    public Result<Transfer> createTransfer(String senderUserId, String senderAccountId, String receiverUserId, String receiverAccountId, int amount) {
        /* 1. Validate the amount is valid */
        if (amount <= 0) {
            return Result.failure("Amount must be greater than 0");
        }

        /* 2. Fetch the sender account */
        Result<Account> senderAccountResult = AccountService.getInstance().getAccount(senderUserId, senderAccountId);
        if (!senderAccountResult.succeeded()) {
            return Result.failure(senderAccountResult.getErrorMessage());
        }
        Account senderAccount = senderAccountResult.getTarget();

        /* 3. Validate the sender has sufficient funds */
        if (senderAccount.getBalance() < amount) {
            return Result.failure("Account " + senderAccountId + " for user " + senderUserId + " has insufficient funds");
        }

        /* 4. Fetch the receiver account */
        Result<Account> receiverAccountResult = AccountService.getInstance().getAccount(receiverUserId, receiverAccountId);
        if (!receiverAccountResult.succeeded()) {
            return Result.failure(receiverAccountResult.getErrorMessage());
        }
        Account receiverAccount = receiverAccountResult.getTarget();

        /* 5. Update each account balance */
        senderAccount.addToBalance(-amount);
        receiverAccount.addToBalance(amount);

        /* 6. Create and register the transaction */
        Transfer transfer = new Transfer(senderUserId, senderAccountId, receiverUserId, receiverAccountId, amount);
        transfersBySenderUserId.get(senderUserId).get(senderAccountId).put(transfer.getId(), transfer);
        transfersByReceiverUserId.get(receiverUserId).get(receiverAccountId).put(transfer.getId(), transfer);
        return Result.success(transfer);
    }
}
