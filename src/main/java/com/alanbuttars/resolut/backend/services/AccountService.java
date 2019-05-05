package com.alanbuttars.resolut.backend.services;

import com.alanbuttars.resolut.backend.models.Account;
import com.alanbuttars.resolut.backend.models.Result;
import com.alanbuttars.resolut.backend.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service for {@link Account} actions.
 */
public class AccountService {

    private static final AccountService INSTANCE = new AccountService();

    private AccountService() {
    }

    /**
     * Returns the singleton instance of this class.
     */
    public static AccountService getInstance() {
        return INSTANCE;
    }

    /**
     * Returns the <code>List</code> of {@link Account}s associated with a given <code>userId</code>, if they exist.
     */
    public Result<List<Account>> listAccounts(String userId) {
        Result<User> userResult = UserService.getInstance().getUser(userId);
        if (!userResult.succeeded()) {
            return Result.failure(userResult.getErrorMessage());
        }
        Map<String, Account> accounts = userResult.getTarget().getAccounts();
        return Result.success(new ArrayList<>(accounts.values()));
    }

    /**
     * Returns the {@link Account} associated with a given <code>userId</code> and <code>accountId</code>, if they exist.
     */
    public Result<Account> getAccount(String userId, String accountId) {
        Result<User> userResult = UserService.getInstance().getUser(userId);
        if (!userResult.succeeded()) {
            return Result.failure(userResult.getErrorMessage());
        }

        Account account = userResult.getTarget().getAccounts().get(accountId);
        if (account == null) {
            return Result.failure("Account " + accountId + " for user " + userId + " could not be found");
        }
        return Result.success(account);
    }
}
