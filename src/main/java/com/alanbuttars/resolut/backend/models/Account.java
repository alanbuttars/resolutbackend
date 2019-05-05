package com.alanbuttars.resolut.backend.models;

/**
 * Simple POJO emulating a balance-holding account owned by a {@link User}.
 */
public class Account {
    private final String id;
    private final String userId;
    private int balance;

    public Account(String id, String userId, int balance) {
        this.id = id;
        this.userId = userId;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public int getBalance() {
        return balance;
    }

    public void addToBalance(int amount) {
        this.balance += amount;
    }

}
