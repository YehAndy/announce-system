package com.example.announce.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

@Component
public class TmpImageCleanupTask {
    private final Path tmpDir = Paths.get("uploads/tmp");

    @Scheduled(cron = "0 0 * * * *") // 每小時執行一次
    public void cleanup() throws IOException {
        if (!Files.exists(tmpDir)) return;

        try (Stream<Path> files = Files.list(tmpDir)) {
            files.filter(Files::isRegularFile).forEach(path -> {
                try {
                    BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
                    FileTime lastModifiedTime = attrs.lastModifiedTime();
                    if (lastModifiedTime.toInstant().isBefore(Instant.now().minus(Duration.ofHours(1)))) {
                        Files.delete(path);
                        System.out.println("刪除過期 tmp 圖片: " + path.getFileName());
                    }
                } catch (IOException e) {
                    System.err.println("刪除失敗: " + e.getMessage());
                }
            });
        }
    }
}
