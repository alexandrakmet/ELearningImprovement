package com.elearning.system.services;

import com.elearning.system.repositories.dao.AnnouncementDao;
import com.elearning.system.repositories.entities.Announcement;
import com.elearning.system.repositories.entities.NotificationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AnnouncementService {
    @Autowired
    private AnnouncementDao announcementDao;

    @Autowired
    private WebSocketSenderService webSocketSenderService;

    public int createAnnouncement(Announcement announcement) {
        announcement.setPublished(true);
        int announcementId = announcementDao.save(announcement);
        if (announcementId == -1) {
            log.info("createAnnouncement: Announcement wasn't saved");
            return -1;
        }
        return announcementId;
    }

    public void updateAnnouncement(Announcement announcement) {
        if (announcement == null) {
            log.info("updateAnnouncement: Announcement is null");
            return;
        }
        webSocketSenderService.sendNotification(announcement.getAuthorId(), announcement.getId(), null,
                NotificationType.CREATED_NEWS);
        announcementDao.update(announcement);
    }

    public void deleteAnnouncement(int announcementId) {
        announcementDao.deleteById(announcementId);
    }

    public Announcement getAnnouncementById(int announcementId) {
        return announcementDao.getById(announcementId);
    }

    public List<Announcement> getAnnouncementsByAuthorLogin(String authorLogin) {
        return announcementDao.getByAuthorLogin(authorLogin);
    }

    public List<Announcement> getAllAnnouncements() {
        return announcementDao.getAllInfo();
    }

    public Page<Announcement> getPageForAllAnnouncements(Optional<Integer> page, Optional<Integer> size) {
        Page<Announcement> announcementPage = announcementDao.getAllInfoForPage(
                PageRequest.of(page.orElse(0), size.orElse(10),
                        Sort.Direction.DESC, "createdDate"));
        return announcementPage;
    }

    public Page<Announcement> getPageForAllAnnouncements(boolean isPublished, Optional<Integer> page, Optional<Integer> size) {
        Page<Announcement> announcementPage = announcementDao.getAllInfoForPage(isPublished,
                PageRequest.of(page.orElse(0), size.orElse(10),
                        Sort.Direction.DESC, "createdDate"));
        return announcementPage;
    }

    public Page<Announcement> getPageByAuthorLogin(String login, Optional<Integer> page, Optional<Integer> size) {
        Page<Announcement> announcementPage = announcementDao.getPageByAuthorLogin(login,
                PageRequest.of(page.orElse(0), size.orElse(10),
                        Sort.Direction.DESC, "createdDate"));
        return announcementPage;
    }

    public Page<Announcement> getPageByAuthorId(int authorId, Optional<Integer> page, Optional<Integer> size) {
        Page<Announcement> announcementPage = announcementDao.getPageByAuthorId(authorId,
                PageRequest.of(page.orElse(0), size.orElse(10),
                        Sort.Direction.DESC, "createdDate"));
        return announcementPage;
    }


}
