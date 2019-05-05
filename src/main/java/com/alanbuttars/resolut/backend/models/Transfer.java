package com.alanbuttars.resolut.backend.models;

import java.util.Date;

/**
 * Simple POJO emulating a monetary transfer between two {@link Account}s.
 */
public class Transfer implements Comparable<Transfer> {
    private static int ID = 0;

    private final int id;
    private final String senderUserId;
    private final String senderAccountId;
    private final String receiverUserId;
    private final String receiverAccountId;
    private final int amount;
    private final Date createdAt;

    /**
     * Initializes this account with an {@link #id} and {@link #createdAt} timestamp along with the given params.
     */
    public Transfer(String senderUserId, String senderAccountId, String receiverUserId, String receiverAccountId, int amount) {
        this.id = ++ID;
        this.senderUserId = senderUserId;
        this.senderAccountId = senderAccountId;
        this.receiverUserId = receiverUserId;
        this.receiverAccountId = receiverAccountId;
        this.amount = amount;
        this.createdAt = new Date();
    }

    public int getId() {
        return id;
    }

    public String getSenderUserId() {
        return senderUserId;
    }

    public String getSenderAccountId() {
        return senderAccountId;
    }

    public String getReceiverUserId() {
        return receiverUserId;
    }

    public String getReceiverAccountId() {
        return receiverAccountId;
    }

    public int getAmount() {
        return amount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public int compareTo(Transfer other) {
        return Integer.compare(this.id, other.id);
    }
}
