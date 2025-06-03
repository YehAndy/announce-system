package com.example.announce.service;

import com.example.announce.entity.Announcement;
import com.example.announce.repo.AnnouncementRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnnouncementService {

    private final AnnouncementRepository announceRepository;

    public AnnouncementService(AnnouncementRepository announceRepository) {
        this.announceRepository = announceRepository;
    }

    public List<Announcement> findAll() {
        return announceRepository.findAll();
    }

    public void save(Announcement announcement) {
        announceRepository.save(announcement);
    }

    public Optional<Announcement> findById(Long id) {
        return announceRepository.findById(id);
    }

    public void update(Announcement announcement) {
        announceRepository.save(announcement);
    }

    public void delete(Long id) {
        announceRepository.deleteById(id);
    }

}
