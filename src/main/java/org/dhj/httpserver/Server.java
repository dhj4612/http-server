package org.dhj.httpserver;

import org.dhj.httpserver.constant.Version;
import org.dhj.httpserver.core.Response;
import org.dhj.httpserver.core.Routers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class Server {
    public static void main(String[] args) throws IOException {
        Routers.addGet("/", request -> new Response()
                .setVersion(Version.HTTP_1_1)
                .setStatus(200)
                .setBody("")
                .setText("OK"));

        Routers.addGet("/hello", _ -> new Response()
                .setVersion(Version.HTTP_1_1)
                .setText("OK")
                .setStatus(200)
                .setHeaders(Map.of("Content-Type", "text/html"))
                .setBody("""
                        <h1>Hello World</h1>
                        """));

        Routers.addPost("/json", _ -> new Response()
                .setVersion(Version.HTTP_1_1)
                .setText("OK")
                .setStatus(200)
                .setHeaders(Map.of("Content-Type", "application/json"))
                .setBody("""
                        {
                        "username": "dhj"
                        }
                        """));

        final int port = 8888;
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.printf("server listening on port %d...\n", port);
            while (true) {
                Socket socket = server.accept();
                Routers.route(socket);
                System.out.println("server process complete...\n");
            }
        }
    }
}
