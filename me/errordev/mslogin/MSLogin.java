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

    public void setHandler(MSLoginHandler handler) {
        this.handler = handler;
    }

    public void start() {
        serverThread = new Thread(() -> {
            try {
                server = HttpServer.create(new InetSocketAddress(5500), 5500);
                server.start();
                server.createContext("/auth", (exchange) -> {
                    final Headers headers = exchange.getRequestHeaders();
                    final String uuid, username, accessToken;
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
                            (accessToken = headers.getFirst("accessToken")) != null) {
                        String response = "Successfully authenticated";
                        exchange.sendResponseHeaders(200, response.getBytes().length);
                        OutputStream os = exchange.getResponseBody();
                        os.write(response.getBytes());
                        os.close();
                        handler.onLoginSuccess(uuid, username, accessToken);
                    } else {
                        String response = "Unsuccessfully authenticated";
                        exchange.sendResponseHeaders(405, response.getBytes().length);
                        OutputStream os = exchange.getResponseBody();
                        os.write(response.getBytes());
                        os.close();
                    }
                });
            } catch (Exception e) {
                throw new RuntimeException("Failed to start server", e);
            }
        });
        server.start();
    }

    public void stop() {
        serverThread.interrupt();
        server.stop(0);
        serverThread = null;
        server = null;
    }

}