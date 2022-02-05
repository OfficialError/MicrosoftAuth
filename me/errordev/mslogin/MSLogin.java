package me.errordev.mslogin;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;

import java.io.OutputStream;
import java.net.InetSocketAddress;

public enum MSLogin {
    INSTANCE;
    private HttpServer server;
    private MSLoginHandler handler;
    private Thread serverThread;
    private boolean running;

    public void setHandler(MSLoginHandler handler) {
        this.handler = handler;
    }

    public void start() {
        stop();
        serverThread = new Thread(() -> {
            try {
                running = true;
                server = HttpServer.create(new InetSocketAddress(5500), 5500);
                server.start();
                server.createContext("/auth", (exchange) -> {
                    final Headers headers = exchange.getRequestHeaders();
                    final String uuid, username, accessToken, clientToken;
                    if (handler == null) {
                        String response = "Authentication handler is null";
                        exchange.sendResponseHeaders(403, response.getBytes().length);
                        OutputStream os = exchange.getResponseBody();
                        os.write(response.getBytes());
                        os.close();
                        return;
                    }
                    if ((uuid = headers.getFirst("uuid")) != null &&
                            (username = headers.getFirst("username")) != null &&
                            (accessToken = headers.getFirst("accessToken")) != null &&
                            (clientToken = headers.getFirst("clientToken")) != null) {
                        String response = "Successfully authenticated";
                        exchange.sendResponseHeaders(200, response.getBytes().length);
                        OutputStream os = exchange.getResponseBody();
                        os.write(response.getBytes());
                        os.close();
                        handler.onLoginSuccess(uuid, username, accessToken, clientToken);
                    } else {
                        String response = "Unsuccessfully authenticated";
                        exchange.sendResponseHeaders(405, response.getBytes().length);
                        OutputStream os = exchange.getResponseBody();
                        os.write(response.getBytes());
                        os.close();
                    }
                });
            } catch (Exception e) {
                running = false;
                throw new RuntimeException("Failed to start server", e);
            }
        });
        serverThread.start();
    }

    public void stop() {
        if (serverThread != null)
            serverThread.interrupt();
        if (server != null)
            server.stop(0);
        serverThread = null;
        server = null;
        running = false;
    }

    public boolean isRunning() {
        return running;
    }
}