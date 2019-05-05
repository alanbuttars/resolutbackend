package com.alanbuttars.resolut.backend;

/**
 * Main entrypoint for this application. For instructions on how to deploy the application, see the <code>README.md</code> file.
 */
public class Main {

    /**
     * Initializes a server that by default will run on host {@link Server#HOST} and port {@link Server#PORT}.
     */
    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}
