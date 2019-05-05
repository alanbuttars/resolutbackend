package com.alanbuttars.resolut.backend.controllers;

import com.alanbuttars.resolut.backend.models.Account;
import com.alanbuttars.resolut.backend.models.Result;
import com.alanbuttars.resolut.backend.services.AccountService;
import io.undertow.server.HttpServerExchange;

import java.util.List;

/**
 * Controller for {@link Account} actions.
 */
public class AccountController extends BaseController {

    /**
     * Sends a response containing an array of {@link Account}s associated with a given user.
     *
     * @param exchange The request containing a <code>userId</code>
     */
    public static void listAccounts(HttpServerExchange exchange) {
        String userId = getParam(exchange, "userId");
        Result<List<Account>> result = AccountService.getInstance().listAccounts(userId);
        if (result.succeeded()) {
            sendJson(exchange, 200, result.getTarget());
        } else {
            sendJson(exchange, 400, result.getErrorMessage());
        }
    }

    /**
     * Sends a response containing a {@link Account} associated with a given ID.
     *
     * @param exchange The request containing a <code>userId</code> and <code>accountId</code>
     */
    public static void getAccount(HttpServerExchange exchange) {
        String userId = getParam(exchange, "userId");
        String accountId = getParam(exchange, "accountId");
        Result<Account> result = AccountService.getInstance().getAccount(userId, accountId);
        if (result.succeeded()) {
            sendJson(exchange, 200, result.getTarget());
        } else {
            sendJson(exchange, 400, result.getErrorMessage());
        }
    }
}
