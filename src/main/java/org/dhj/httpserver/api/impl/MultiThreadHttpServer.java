package org.dhj.httpserver.api.impl;

import org.dhj.httpserver.api.HttpServer;
import org.dhj.httpserver.core.Routers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class MultiThreadHttpServer implements HttpServer {

    private static final Logger logger = Logger.getLogger(MultiThreadHttpServer.class.getName());
    private static final ExecutorService executor = Executors.newFixedThreadPool(10);
    private static volatile boolean stopped = false;
    private static ServerSocket serverSocket = null;

    @Override
    public void start(int port) {
        new Thread(() -> { // async start
            try (final ServerSocket instance = new ServerSocket(port)) {
                serverSocket = instance;
                System.out.printf("Server listening on port %d...\n\n", port);
                while (!stopped) {
                    Socket client = serverSocket.accept();
                    executor.execute(() -> {
                        System.out.println("server-pool-" + Thread.currentThread().getName());
                        try {
                            long start = System.currentTimeMillis();
                            Routers.route(client);
                            long end = System.currentTimeMillis();
                            System.out.printf("""
                                    Server process complete, time: %dms
                                    
                                    """, (end - start));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    @Override
    public synchronized void stop() {
        if (stopped) {
            return;
        }
        stopped = true;
        executor.shutdown();
        try {
            if (!executor.awaitTermination(1, TimeUnit.HOURS)) {
                logger.info("Server closed timeout");
            }
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (InterruptedException | IOException e) {
            executor.shutdownNow();
            throw new RuntimeException(e);
        }
    }
}
