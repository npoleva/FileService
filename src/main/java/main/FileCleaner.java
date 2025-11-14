package main;

import java.io.File;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class FileCleaner implements Runnable {
    private final Map<String, FileInfo> files;
    private final String uploadDir;

    public FileCleaner(Map<String, FileInfo> files, String uploadDir) {
        this.files = files;
        this.uploadDir = uploadDir;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(24 * 60 * 60 * 1000);
                LocalDateTime now = LocalDateTime.now();
                for (String key : files.keySet()) {
                    FileInfo info = files.get(key);
                    if (info != null && ChronoUnit.DAYS.between(info.getLastAccess(), now) > 30) {
                        new File(info.getFilePath()).delete();
                        files.remove(key);
                        System.out.println("Deleted file: " + info.getFileName());
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
