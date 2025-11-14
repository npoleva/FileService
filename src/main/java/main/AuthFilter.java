package main;

import com.sun.net.httpserver.HttpExchange;

public class AuthFilter {
    private static final String TOKEN = "SECRET_TOKEN";

    public static boolean isAuthorized(HttpExchange exchange) {
        String auth = exchange.getRequestHeaders().getFirst("Authorization");
        return auth != null && auth.equals("Bearer " + TOKEN);
    }
}
