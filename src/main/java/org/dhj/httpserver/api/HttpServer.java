package org.dhj.httpserver.api;

public interface HttpServer {
    void start(int port);

    void stop();
}
