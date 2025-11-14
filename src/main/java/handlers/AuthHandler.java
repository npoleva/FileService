package handlers;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.List;

public class AuthHandler extends Handler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        List<String> cookies = exchange.getRequestHeaders().get("Cookie");
        boolean authorized = false;
        if (cookies != null) {
            for (String cookie : cookies) {
                if (cookie.contains("auth=SECRET_TOKEN")) {
                    authorized = true;
                    break;
                }
            }
        }
        if (!authorized) {
            exchange.sendResponseHeaders(401, -1);
            return;
        }
        next.handle(exchange);
    }
}


