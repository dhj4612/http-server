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
        response.append(version.toString()).append(" ")
                .append(status).append(" ")
                .append(text)
                .append("\r\n");

        if (headers != null && !headers.isEmpty()) {
            String headers = this.headers.entrySet()
                    .stream()
                    .map(entry -> entry.getKey() + ": " + entry.getValue())
                    .collect(Collectors.joining("\r\n"));
            response.append(headers).append("\r\n");
        }

        if (body != null && !body.isEmpty()) {
            response.append("\r\n").append(body);
        } else {
            response.append("\r\n");
        }

        System.out.printf("response => \n%s", response);
        return response.toString().trim();
    }
}
