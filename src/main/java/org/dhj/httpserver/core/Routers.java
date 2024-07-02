package org.dhj.httpserver.core;

import org.dhj.httpserver.api.Handle;
import org.dhj.httpserver.constant.Method;
import org.dhj.httpserver.constant.Version;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Routers {
    private static final List<Route> routes = new ArrayList<>(10);

    public static void addGet(String path, Handle handle) {
        routes.add(new Route(path, Method.GET, handle));
    }

    public static void addPost(String path, Handle handle) {
        routes.add(new Route(path, Method.POST, handle));
    }

    public static void add(Method method, String path, Handle handle) {
        routes.add(new Route(path, method, handle));
    }

    public static void route(Socket socket) throws IOException {
        Request request = Request.parse(socket.getInputStream());
        System.out.printf("request => %s\n", request.toString());

        Route handler = routes.stream()
                .filter(route ->
                        route.method().equals(request.getMethod())
                                && route.path().equals(request.getPath())
                )
                .findFirst()
                .orElse(null);

        OutputStream write = socket.getOutputStream();
        if (handler == null) {
            write.write(new Response()
                    .setVersion(Version.HTTP_1_1)
                    .setStatus(404)
                    .setText("NotFound")
                    .setHeaders(Map.of("Content-Type", "text/html", "Connection", "close"))
                    .setBody("""
                            <h1>404 NotFound</h1>
                            """)
                    .toString()
                    .getBytes(StandardCharsets.UTF_8));
        } else {
            write.write(handler.handle().handle(request).toString().getBytes(StandardCharsets.UTF_8));
        }
        write.flush();
        socket.close();
    }
}
