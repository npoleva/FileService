package main;

import java.time.LocalDateTime;

public class FileInfo {
    private final String fileName;
    private final String filePath;
    private final String downloadKey;
    private final long size;
    private LocalDateTime lastAccess;
    private final LocalDateTime uploadTime;
    private int downloadCount;

    public FileInfo(String fileName, String filePath, String downloadKey, long size) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.downloadKey = downloadKey;
        this.size = size;
        this.uploadTime = LocalDateTime.now();
        this.lastAccess = LocalDateTime.now();
        this.downloadCount = 0;
    }

    public String getFileName() { return fileName; }
    public String getFilePath() { return filePath; }
    public String getDownloadKey() { return downloadKey; }
    public long getSize() { return size; }
    public LocalDateTime getLastAccess() { return lastAccess; }
    public LocalDateTime getUploadTime() { return uploadTime; }
    public int getDownloadCount() { return downloadCount; }

    public void updateAccessTime() {
        this.lastAccess = LocalDateTime.now();
        this.downloadCount++;
    }
}
