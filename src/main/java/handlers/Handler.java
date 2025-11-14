package handlers;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;

public abstract class Handler {
    protected Handler next;

    public Handler setNext(Handler next) {
        this.next = next;
        return next;
    }

    public abstract void handle(HttpExchange exchange) throws IOException;
}

