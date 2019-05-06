package com.alanbuttars.resolut.backend.serializers;

import com.alanbuttars.resolut.backend.Server;
import com.alanbuttars.resolut.backend.models.Account;
import com.alanbuttars.resolut.backend.models.Transfer;
import com.alanbuttars.resolut.backend.models.User;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Common serializer for all REST links for application objects.
 */
public class LinkSerializer {

    /**
     * Serializes the REST links for a {@link User}.
     */
    public JsonArray serialize(User user) {
        JsonArray links = new JsonArray();
        links.add(serializeGetUser("self", user.getId()));
        links.add(serializeGetAccounts("showAccounts", user.getId()));
        return links;
    }

    /**
     * Serializes the REST links for a {@link Account}.
     */
    public JsonArray serialize(Account account) {
        JsonArray links = new JsonArray();
        links.add(serializeGetAccount("self", account.getUserId(), account.getId()));
        links.add(serializeGetUser("showUser", account.getUserId()));
        links.add(serializeGetTransfers("showTransfers", account.getUserId(), account.getId()));
        links.add(serializePostTransfer("createTransfer", account.getUserId(), account.getId()));
        return links;
    }

    /**
     * Serializes the REST links for a {@link Transfer}
     */
    public JsonArray serialize(Transfer transfer) {
        JsonArray links = new JsonArray();
        links.add(serializeGetTransfer("self", transfer.getReceiverUserId(), transfer.getReceiverAccountId(), transfer.getId()));
        links.add(serializeGetUser("showSenderUser", transfer.getSenderUserId()));
        links.add(serializeGetAccount("showSenderAccount", transfer.getSenderUserId(), transfer.getSenderAccountId()));
        links.add(serializeGetUser("showReceiverUser", transfer.getReceiverUserId()));
        links.add(serializeGetAccount("showReceiverAccount", transfer.getReceiverUserId(), transfer.getReceiverAccountId()));

        return links;
    }

    private JsonObject serializeGetUser(String rel, String userId) {
        return serializeGet(rel, "users", userId);
    }

    private JsonObject serializeGetAccounts(String rel, String userId) {
        return serializeGet(rel, "users", userId, "accounts");
    }

    private JsonObject serializeGetAccount(String rel, String userId, String accountId) {
        return serializeGet(rel, "users", userId, "accounts", accountId);
    }

    private JsonObject serializeGetTransfers(String rel, String userId, String accountId) {
        return serializeGet(rel, "users", userId, "accounts", accountId, "transfers");
    }

    private JsonObject serializeGetTransfer(String rel, String userId, String accountId, int transferId) {
        return serializeGet(rel, "users", userId, "accounts", accountId, "transfers", "" + transferId);
    }

    private JsonObject serializePostTransfer(String rel, String userId, String accountId) {
        return serializePost(rel, "users", userId, "accounts", accountId, "transfers");
    }

    private JsonObject serializeGet(String rel, String... path) {
        return serialize("GET", rel, path);
    }

    private JsonObject serializePost(String rel, String... path) {
        return serialize("POST", rel, path);
    }

    private JsonObject serialize(String method, String rel, String... path) {
        JsonObject link = new JsonObject();
        link.addProperty("rel", rel);
        link.addProperty("method", method);
        link.addProperty("href", Server.basename() + "/" + String.join("/", path));
        return link;
    }
}
