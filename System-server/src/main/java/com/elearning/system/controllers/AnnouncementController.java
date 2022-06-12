package com.elearning.system.controllers;

import com.elearning.system.services.AnnouncementService;
import com.elearning.system.repositories.entities.Announcement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("api/v1")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @PostMapping("/announcement")
    public int createAnnouncement(@RequestBody Announcement announcement) {
        return announcementService.createAnnouncement(announcement);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('MODERATOR')")
    @PutMapping("/announcement")
    public void updateAnnouncement(@RequestBody Announcement announcement) {
        announcementService.updateAnnouncement(announcement);
    }

    @GetMapping("/announcement/{id}")
    public Announcement getAnnouncement(@PathVariable int id) {
        return announcementService.getAnnouncementById(id);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('MODERATOR')")
    @DeleteMapping("/announcement/{id}")
    public void deleteAnnouncement(@PathVariable int id) {
        announcementService.deleteAnnouncement(id);
    }

    @GetMapping("/announcements")
    public List<Announcement> getAnnouncements(@RequestParam(value = "pageNumber") int pageNumber,
                                               @RequestParam(value = "isPublished") boolean isPublished) {
        return announcementService.getPageForAllAnnouncements(isPublished, Optional.of(pageNumber), Optional.of(20)).toList();
    }

    @GetMapping("/groupAnnouncements")
    public List<Announcement> getAnnouncementsById(@RequestParam(value = "pageNumber") int pageNumber,
                                               @RequestParam(value = "authorId") int authorId) {
        return announcementService.getPageByAuthorId(authorId, Optional.of(pageNumber), Optional.of(20)).toList();
    }
}
