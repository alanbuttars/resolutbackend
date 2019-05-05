package com.alanbuttars.resolut.backend.controllers;

import com.alanbuttars.resolut.backend.Utils;
import com.alanbuttars.resolut.backend.models.Error;
import com.alanbuttars.resolut.backend.services.SerializerService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import java.io.IOException;

/**
 * Common controller for all actions.
 */
public class BaseController {

    /**
     * Fetches the query parameter value for a given key, if it exists.
     *
     * @param exchange
     * @param key      The query key
     */
    public static String getParam(HttpServerExchange exchange, String key) {
        return exchange.getQueryParameters().get(key).peekFirst();
    }

    /**
     * Parses the query payload value and wraps it in a {@link JsonObject}.
     *
     * @param exchange
     */
    public static JsonObject getPayload(HttpServerExchange exchange) {
        try {
            String json = Utils.readResponse(exchange.getInputStream());
            return new JsonParser().parse(json).getAsJsonObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the appropriate data for a JSON response.
     *
     * @param exchange
     * @param statusCode
     * @param object     The serializable object
     */
    public static void sendJson(HttpServerExchange exchange, int statusCode, Object object) {
        exchange.setStatusCode(statusCode);
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
        exchange.getResponseSender().send(SerializerService.getInstance().toJson(object));
    }

    public static void sendJson(HttpServerExchange exchange, int statusCode, String errorMessage) {
        sendJson(exchange, statusCode, new Error(errorMessage));
    }
}
