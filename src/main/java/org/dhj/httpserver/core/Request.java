package org.dhj.httpserver.core;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.dhj.httpserver.constant.Method;
import org.dhj.httpserver.constant.Version;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Accessors(chain = true)
@Getter
@Setter
@ToString
public final class Request {
    private Version version;
    private Method method;
    private String path;
    private Map<String, String> headers;
    private String body;

    public static Request parse(InputStream is) throws IOException {
        StringBuilder request = new StringBuilder();
        String line;
        BufferedReader buffer = new BufferedReader(new InputStreamReader(is));

        // 忽略空行，空行之后全部为body部分，单独处理
        while ((line = buffer.readLine()) != null && !line.isEmpty()) {
            request.append(line).append("\n");
        }

        Method method = Method.UNKNOWN;
        Version version = Version.UNKNOWN;
        String path = null;
        String body = null;
        Map<String, String> headers = null;
        List<String> lines = request.toString().lines().toList();
        for (int i = 0; i < lines.size(); i++) {
            final String requestLine = lines.get(i);
            if (i == 0) {
                String[] splitWithSpace = requestLine.split(" ");
                method = Method.parse(splitWithSpace[0]);
                path = splitWithSpace[1];
                version = Version.parse(splitWithSpace[2]);
            } else if (requestLine.contains(": ")) { // request header part
                String[] split = requestLine.split(": ");
                if (headers == null) {
                    headers = new HashMap<>(16);
                }
                headers.put(split[0], split[1]);
            }
        }

        // read the body
        if (headers != null) {
            if (headers.containsKey("Content-Length") || headers.containsKey("content-length")) {
                int len = Integer.parseInt(headers.getOrDefault("Content-Length",
                        headers.getOrDefault("content-length", "0")));
                char[] chars = new char[len];
                int read = buffer.read(chars, 0, len);
                if (read != -1) {
                    body = new String(chars).trim();
                }
            }
        }

        return new Request()
                .setMethod(method)
                .setVersion(version)
                .setPath(path)
                .setBody(body)
                .setHeaders(headers);
    }
}
