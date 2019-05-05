package com.alanbuttars.resolut.backend.controllers;

import com.alanbuttars.resolut.backend.models.Result;
import com.alanbuttars.resolut.backend.models.User;
import com.alanbuttars.resolut.backend.services.UserService;
import io.undertow.server.HttpServerExchange;

import java.util.List;

/**
 * Controller for {@link User} actions.
 */
public class UserController extends BaseController {

    /**
     * Sends a response containing an array of {@link User}s.
     *
     * @param exchange
     */
    public static void listUsers(HttpServerExchange exchange) {
        Result<List<User>> result = UserService.getInstance().listUsers();
        if (result.succeeded()) {
            sendJson(exchange, 200, result.getTarget());
        } else {
            sendJson(exchange, 400, result.getErrorMessage());
        }
    }

    /**
     * Sends a response containing a {@link User} associated with a given ID.
     *
     * @param exchange The request containing a <code>userId</code>
     */
    public static void getUser(HttpServerExchange exchange) {
        String userId = getParam(exchange, "userId");
        Result<User> result = UserService.getInstance().getUser(userId);
        if (result.succeeded()) {
            sendJson(exchange, 200, result.getTarget());
        } else {
            sendJson(exchange, 400, result.getErrorMessage());
        }
    }
}
