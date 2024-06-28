package org.dhj.httpserver.constant;

public enum Version {
    HTTP_1_1,
    UNKNOWN;

    public static Version parse(String version) {
        return switch (version) {
            case "HTTP/1.1" -> HTTP_1_1;
            default -> UNKNOWN;
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case HTTP_1_1 -> "HTTP/1.1";
            default -> UNKNOWN.name();
        };
    }
}
