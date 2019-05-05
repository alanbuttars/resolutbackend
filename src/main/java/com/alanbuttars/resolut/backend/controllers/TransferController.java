package com.alanbuttars.resolut.backend.controllers;

import com.alanbuttars.resolut.backend.models.Result;
import com.alanbuttars.resolut.backend.models.Transfer;
import com.alanbuttars.resolut.backend.services.TransferService;
import com.google.gson.JsonObject;
import io.undertow.server.HttpServerExchange;

import java.util.List;

/**
 * Controller for {@link Transfer} actions.
 */
public class TransferController extends BaseController {

    /**
     * Sends a response containing an array of {@link Transfer}s associated with a given user and account.
     *
     * @param exchange The request containing a <code>userId</code> and <code>accountId</code>
     */
    public static void listTransfers(HttpServerExchange exchange) {
        String userId = getParam(exchange, "userId");
        String accountId = getParam(exchange, "accountId");
        Result<List<Transfer>> result = TransferService.getInstance().listTransfers(userId, accountId);
        if (result.succeeded()) {
            sendJson(exchange, 200, result.getTarget());
        } else {
            sendJson(exchange, 400, result.getErrorMessage());
        }
    }

    /**
     * Sends a response containing a {@link Transfer} associated with a given ID.
     *
     * @param exchange The request containing a <code>userId</code>, <code>accountId</code>, and <code>transferId</code>
     */
    public static void getTransfer(HttpServerExchange exchange) {
        String userId = getParam(exchange, "userId");
        String accountId = getParam(exchange, "accountId");
        String transferId = getParam(exchange, "transferId");
        Result<Transfer> result = TransferService.getInstance().getTransfer(userId, accountId, transferId);
        if (result.succeeded()) {
            sendJson(exchange, 200, result.getTarget());
        } else {
            sendJson(exchange, 400, result.getErrorMessage());
        }
    }

    /**
     * Creates a {@link Transfer} with a given payload, then sends it as a response.
     *
     * @param exchange The request containing a <code>userId</code>, <code>accountId</code>, and payload
     */
    public static void createTransfer(HttpServerExchange exchange) {
        String userId = getParam(exchange, "userId");
        String accountId = getParam(exchange, "accountId");

        JsonObject json = getPayload(exchange);
        String receiverUserId = json.get("receiverUserId").getAsString();
        String receiverAccountId = json.get("receiverAccountId").getAsString();
        int amount = json.get("amount").getAsInt();

        Result<Transfer> result = TransferService.getInstance().createTransfer(userId, accountId, receiverUserId, receiverAccountId, amount);
        if (result.succeeded()) {
            sendJson(exchange, 200, result.getTarget());
        } else {
            sendJson(exchange, 400, result.getErrorMessage());
        }
    }
}
