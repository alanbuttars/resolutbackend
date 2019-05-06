package com.alanbuttars.resolut.backend.models;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simple POJO emulating a balance-holding account owned by a {@link User}.
 */
public class Account {
    private final String id;
    private final String userId;
    /* Atomic to prevent a race condition against an account balance */
    private AtomicInteger balance;

    public Account(String id, String userId, int balance) {
        this.id = id;
        this.userId = userId;
        this.balance = new AtomicInteger(balance);
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public int getBalance() {
        return balance.get();
    }

    public void addToBalance(int amount) {
        this.balance.getAndAdd(amount);
    }

}
