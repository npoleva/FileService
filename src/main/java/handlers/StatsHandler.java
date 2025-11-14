package handlers;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import main.FileInfo;

public class StatsHandler extends Handler {
    private final Map<String, FileInfo> files;

    public StatsHandler(Map<String, FileInfo> files) {
        this.files = files;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("File statistics:\n");
        for (FileInfo info : files.values()) {
            sb.append("Name: ").append(info.getFileName())
                    .append(", Size: ").append(info.getSize())
                    .append(" bytes, Uploaded: ").append(info.getUploadTime())
                    .append(", Downloads: ").append(info.getDownloadCount())
                    .append("\n");
        }

        byte[] bytes = sb.toString().getBytes();
        exchange.sendResponseHeaders(200, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }
}

