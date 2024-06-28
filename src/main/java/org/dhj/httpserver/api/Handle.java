package org.dhj.httpserver.api;

import org.dhj.httpserver.core.Request;
import org.dhj.httpserver.core.Response;

public interface Handle {
    Response handle(Request request);
}
