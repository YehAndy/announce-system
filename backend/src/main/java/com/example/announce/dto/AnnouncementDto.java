package com.example.announce.dto;

import com.example.announce.entity.Announcement;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AnnouncementDto {
    public Long id;
    public String title;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Taipei")
    public LocalDate createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Taipei")
    public LocalDate expireAt;
    public String content;
    public String publisher;
    private List<String> attachments = new ArrayList<>();;

    public static AnnouncementDto from(Announcement ann) {
        AnnouncementDto dto = new AnnouncementDto();
        dto.id = ann.getId();
        dto.title = ann.getTitle();
        dto.createdAt = ann.getCreatedAt();
        dto.expireAt = ann.getExpireAt();
        dto.publisher = ann.getPublisher();
        dto.content = ann.getContent();
        String attachmentsJson = ann.getAttachments();
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> attachmentList = null;
        try {
            attachmentList = objectMapper.readValue(
                    attachmentsJson,
                    new TypeReference<List<String>>() {}
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        dto.setAttachments(attachmentList);
        return dto;
    }

}
