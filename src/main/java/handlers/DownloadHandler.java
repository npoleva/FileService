package handlers;

import com.sun.net.httpserver.HttpExchange;
import main.FileInfo;

import java.io.*;
import java.nio.file.*;
import java.util.Map;

public class DownloadHandler extends Handler {
    private final Map<String, FileInfo> files;

    public DownloadHandler(Map<String, FileInfo> files) {
        this.files = files;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query == null || !query.contains("key=")) {
            exchange.sendResponseHeaders(400, -1);
            return;
        }

        String key = query.split("=")[1];
        FileInfo info = files.get(key);
        if (info == null) {
            exchange.sendResponseHeaders(404, -1);
            return;
        }

        info.updateAccessTime();

        File file = new File(info.getFilePath());
        exchange.getResponseHeaders().add("Content-Disposition", "attachment; filename=\"" + info.getFileName() + "\"");
        exchange.sendResponseHeaders(200, file.length());

        OutputStream os = exchange.getResponseBody();
        Files.copy(file.toPath(), os);
        os.close();
    }
}

