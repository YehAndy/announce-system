package com.example.announce.controller;

import com.example.announce.dto.AnnouncementDto;
import com.example.announce.entity.Announcement;
import com.example.announce.repo.AnnouncementRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {

    @Autowired
    private AnnouncementRepository repo;

    @PostMapping("/publish")
    public ResponseEntity<String> publish(@RequestBody AnnouncementDto dto) throws JsonProcessingException {
        Announcement ann = new Announcement();
        ann.setTitle(dto.getTitle());
        ann.setContent(dto.getContent());
        ann.setCreatedAt(dto.getCreatedAt()); // 若 dto 是 LocalDate
        ann.setExpireAt(dto.getExpireAt());
        ann.setPublisher(dto.getPublisher());
        ObjectMapper objectMapper = new ObjectMapper();
        ann.setAttachments(objectMapper.writeValueAsString(dto.getAttachments()));

        repo.save(ann);
        return ResponseEntity.ok("新增成功");
    }

    @GetMapping("/list")
    public Page<AnnouncementDto> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return repo.findAll(pageable).map(AnnouncementDto::from);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnnouncementDto> getById(@PathVariable Long id) {
        return repo.findById(id)
                .map(AnnouncementDto::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody AnnouncementDto dto) {
        ObjectMapper objectMapper = new ObjectMapper();
        return repo.findById(dto.getId())
                .map(entity -> {
                    entity.setTitle(dto.getTitle());
                    entity.setPublisher(dto.getPublisher());
                    entity.setContent(dto.getContent());
                    entity.setCreatedAt(dto.getCreatedAt());
                    entity.setExpireAt(dto.getExpireAt());
                    try {
                        entity.setAttachments(objectMapper.writeValueAsString(dto.getAttachments()));
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    repo.save(entity);
                    return ResponseEntity.ok("更新成功");
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return ResponseEntity.ok("刪除成功");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/uploadAttachment")
    public ResponseEntity<Map<String, String>> uploadAttachment(@RequestParam("file") MultipartFile file) {
        try {
            String uploadDir = "uploads/files/";
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadDir + filename);
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());

            String url = "/uploads/files/" + filename;
            return ResponseEntity.ok(Map.of("fileName", filename, "url", url));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) throws IOException {
        Path filePath = Paths.get("uploads/files/").resolve(fileName);
        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new UrlResource(filePath.toUri());
        String contentType = Files.probeContentType(filePath);
        if (contentType == null) contentType = "application/octet-stream";

        // 🎯 去掉 UUID，只取後面的原始檔名
        String originalFileName = fileName.substring(fileName.indexOf('_') + 1);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + originalFileName + "\"")
                .body(resource);
    }

}