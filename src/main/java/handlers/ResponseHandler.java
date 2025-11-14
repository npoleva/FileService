package handlers;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;

public class ResponseHandler extends Handler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Object keyObj = exchange.getAttribute("downloadKey");
        if (keyObj == null) {
            exchange.sendResponseHeaders(500, -1);
            return;
        }
        String key = keyObj.toString();
        String response = "File uploaded! Download link: http://localhost:8000/download?key=" + key;
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}

