package com.alanbuttars.resolut.backend;

import com.alanbuttars.resolut.backend.controllers.AccountController;
import com.alanbuttars.resolut.backend.controllers.TransferController;
import com.alanbuttars.resolut.backend.controllers.UserController;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.BlockingHandler;
import io.undertow.server.handlers.accesslog.AccessLogHandler;
import io.undertow.server.handlers.accesslog.AccessLogReceiver;
import io.undertow.server.handlers.accesslog.DefaultAccessLogReceiver;

import java.io.File;
import java.nio.file.Paths;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Emulates a server which can be deployed and killed at will.
 */
public class Server {

    public static final String HOST = "localhost";
    public static final int PORT = 8080;

    /* The underlying server */
    private final Undertow undertow;

    public Server() {
        AccessLogReceiver accessLogReceiver = createAccessLogReceiver();
        HttpHandler routingHandler = createRoutingHandler();
        HttpHandler rootHandler = createRootHandler(routingHandler, accessLogReceiver);

        this.undertow = Undertow.builder()//
                .addHttpListener(PORT, HOST, rootHandler)//
                .build();
    }

    /**
     * Returns a {@link HttpHandler} capable of dispatching worker threads.
     */
    private static HttpHandler handle(HttpHandler next) {
        return new BlockingHandler(next);
    }

    /**
     * Create access log receiver and send logs to <code>APPLICATION_DIRECTORY/log/access.log</code>.
     */
    private AccessLogReceiver createAccessLogReceiver() {
        Executor logWriterExecutor = Executors.newCachedThreadPool();
        File logDirectory = Paths.get("log").toFile();
        logDirectory.mkdir();
        return new DefaultAccessLogReceiver(logWriterExecutor, logDirectory, "access.", "log");
    }

    /**
     * Create {@link HttpHandler} to route HTTP requests.
     */
    private HttpHandler createRoutingHandler() {
        return new RoutingHandler()
                .get("/users", handle(UserController::listUsers))//
                .get("/users/{userId}", handle(UserController::getUser))//
                .get("/users/{userId}/accounts", handle(AccountController::listAccounts))//
                .get("/users/{userId}/accounts/{accountId}", handle(AccountController::getAccount))//
                .get("/users/{userId}/accounts/{accountId}/transfers", handle(TransferController::listTransfers))//
                .get("/users/{userId}/accounts/{accountId}/transfers/{transferId}", handle(TransferController::getTransfer))//
                .post("/users/{userId}/accounts/{accountId}/transfers", handle(TransferController::createTransfer));
    }

    /**
     * Create root-level {@link HttpHandler}.
     */
    public HttpHandler createRootHandler(HttpHandler nextHandler, AccessLogReceiver accessLogReceiver) {
        return new AccessLogHandler(nextHandler, accessLogReceiver, "combined", Undertow.class.getClassLoader());
    }

    /**
     * Starts the server on host {@link #HOST} and port {@link #PORT}.
     */
    public void start() {
        undertow.start();
    }

    /**
     * Stops the server.
     */
    public void stop() {
        undertow.stop();
    }
}
