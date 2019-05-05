package com.alanbuttars.resolut.backend.models;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Simple POJO emulating a user with one or more {@link Account}s.
 */
public class User {

    private final String id;
    private final Map<String, Account> accounts;

    public User(String id, List<Account> accounts) {
        this.id = id;
        this.accounts = new TreeMap<>();
        accounts.stream().forEach(account -> {
            this.accounts.put(account.getId(), account);
        });
    }

    public String getId() {
        return id;
    }

    public Map<String, Account> getAccounts() {
        return accounts;
    }
}
