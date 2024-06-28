package org.dhj.httpserver.constant;

public enum Method {
    GET,
    POST,
    UNKNOWN;

    public static Method parse(String method) {
        return switch (method) {
            case "GET" -> GET;
            case "POST" -> POST;
            default -> UNKNOWN;
        };
    }
}
