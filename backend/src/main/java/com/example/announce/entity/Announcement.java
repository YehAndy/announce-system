package com.example.announce.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "announcement")
@Setter
@Getter
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String publisher;
    private LocalDate createdAt;
    private LocalDate expireAt;
    @Column(columnDefinition = "TEXT")
    private String content;

    private String attachments;  // 儲存附件檔名（多檔用逗號隔開）


// getters and setters omitted for brevity
}