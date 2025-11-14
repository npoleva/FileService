package handlers;

import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.nio.file.*;
import java.util.Map;
import java.util.UUID;
import main.FileInfo;

public class UploadHandler extends Handler {
    private final Map<String, FileInfo> files;
    private final String uploadDir;

    public UploadHandler(Map<String, FileInfo> files, String uploadDir) {
        this.files = files;
        this.uploadDir = uploadDir;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        String boundary = exchange.getRequestHeaders().getFirst("Content-type")
                .split("boundary=")[1];

        InputStream is = exchange.getRequestBody();
        byte[] body = is.readAllBytes();
        String content = new String(body);

        String[] parts = content.split("--" + boundary);
        for (String part : parts) {
            if (part.contains("filename=\"")) {
                String fileName = part.split("filename=\"")[1].split("\"")[0];
                int index = part.indexOf("\r\n\r\n");
                byte[] fileData = part.substring(index + 4, part.length() - 2).getBytes();

                String key = UUID.randomUUID().toString();
                Path filePath = Paths.get(uploadDir, fileName);
                Files.write(filePath, fileData);

                FileInfo info = new FileInfo(fileName, filePath.toString(), key, fileData.length);
                files.put(key, info);

                exchange.setAttribute("downloadKey", key);
                if (next != null) next.handle(exchange);
                return;
            }
        }
        exchange.sendResponseHeaders(400, -1);
    }
}

