package org.dhj.httpserver.core;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.dhj.httpserver.constant.Version;

import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@Accessors(chain = true)
public class Response {
    private Version version;
    private int status;
    private String text;
    private Map<String, String> headers;
    private String body;

    @Override
    public String toString() {
        StringBuilder response = new StringBuilder();
        response.append(version.toString())
                .append(" ").append(status).append(" ")
                .append(text).append("\n");

        if (headers != null && !headers.isEmpty()) {
            String headers = this.headers.entrySet()
                    .stream()
                    .map(entry -> entry.getKey() + ": " + entry.getValue())
                    .collect(Collectors.joining("\n"));
            response.append(headers).append("\n");
        }

        if (body != null && !body.isEmpty()) {
            response.append("\n").append(body);
        } else {
            response.append("\n");
        }

        System.out.printf("response => %s", response);
        return response.toString().trim();
    }
}
