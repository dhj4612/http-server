package org.dhj.httpserver.core;

import org.dhj.httpserver.api.Handle;
import org.dhj.httpserver.constant.Method;

public record Route(String path, Method method, Handle handle) {
}
