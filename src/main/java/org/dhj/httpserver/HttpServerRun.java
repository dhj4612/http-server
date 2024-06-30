package org.dhj.httpserver;

import org.dhj.httpserver.api.HttpServer;
import org.dhj.httpserver.api.impl.MultiThreadHttpServer;
import org.dhj.httpserver.constant.Version;
import org.dhj.httpserver.core.Response;
import org.dhj.httpserver.core.Routers;

import java.util.Map;

public class HttpServerRun {

    public static void main(String[] args) {
        Routers.addGet("/", _ -> new Response()
                .setVersion(Version.HTTP_1_1)
                .setStatus(200)
                .setText("OK")
                .setHeaders(Map.of("Content-Type", "text/plain", "Content-Length", "0"))
                .setBody("")
        );

        Routers.addGet("/hello", _ -> new Response()
                .setVersion(Version.HTTP_1_1)
                .setStatus(200)
                .setText("OK")
                .setHeaders(Map.of("Content-Type", "text/html"))
                .setBody("""
                        <h1>Hello World</h1>
                        """));

        Routers.addPost("/json", _ -> new Response()
                .setVersion(Version.HTTP_1_1)
                .setStatus(200)
                .setText("OK")
                .setHeaders(Map.of("Content-Type", "application/json"))
                .setBody("""
                        {
                        "username": "dhj"
                        }
                        """));

        final int port = 8888;
        HttpServer httpServer = new MultiThreadHttpServer();
        httpServer.start(port);
    }
}
