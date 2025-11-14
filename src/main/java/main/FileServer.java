package main;

import com.sun.net.httpserver.HttpServer;
import handlers.*;

import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class FileServer {
    private static final int PORT = 8000;
    private static final String UPLOAD_DIR = "files";
    private static final Map<String, FileInfo> files = new ConcurrentHashMap<>();

    public static void main(String[] args) throws Exception {
        Files.createDirectories(Paths.get(UPLOAD_DIR));

        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        server.createContext("/login", exchange -> {
            if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }
            exchange.getResponseHeaders().add(
                    "Set-Cookie", "auth=SECRET_TOKEN; Path=/; HttpOnly"
            );

            String response = "Login successful. Cookie set!";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        });

        server.createContext("/upload", exchange -> {
            if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }
            new UploadHandler(files, UPLOAD_DIR).handle(exchange);
            new ResponseHandler().handle(exchange);
        });


        server.createContext("/download", exchange -> {
            Handler chain = new AuthHandler();
            chain.setNext(new DownloadHandler(files));
            chain.handle(exchange);
        });

        server.createContext("/stats", exchange -> {
            Handler chain = new AuthHandler();
            chain.setNext(new StatsHandler(files));
            chain.handle(exchange);
        });

        server.createContext("/", exchange -> {
            String uri = exchange.getRequestURI().getPath();
            if (uri.equals("/")) uri = "/index.html";

            Path filePath = Paths.get("src/main/resources" + uri);
            if (!Files.exists(filePath)) {
                exchange.sendResponseHeaders(404, -1);
                return;
            }

            String contentType = "text/plain";
            if (uri.endsWith(".html")) contentType = "text/html";
            else if (uri.endsWith(".js")) contentType = "application/javascript";
            else if (uri.endsWith(".css")) contentType = "text/css";

            exchange.getResponseHeaders().set("Content-Type", contentType);
            byte[] bytes = Files.readAllBytes(filePath);
            exchange.sendResponseHeaders(200, bytes.length);
            exchange.getResponseBody().write(bytes);
            exchange.getResponseBody().close();
        });


        new Thread(new FileCleaner(files, UPLOAD_DIR)).start();

        server.setExecutor(null);
        server.start();
        System.out.println("Server started at http://localhost:" + PORT);
    }
}
