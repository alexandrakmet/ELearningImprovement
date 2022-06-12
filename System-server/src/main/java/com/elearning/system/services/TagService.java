package com.elearning.system.services;

import com.elearning.system.repositories.dao.TagDao;
import com.elearning.system.repositories.entities.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TagService {
    @Autowired
    private TagDao tagDao;

    public List<Tag> getTagsByQuizId(int quizId) {
        return tagDao.getByQuizId(quizId);
    }

    public int addTag(Tag tag) {
        int tagId = -1;
        String tagName = tag.getName();
        if (tagName != null && !tagName.isEmpty()) {
            tagId = tagDao.getIdByName(tagName);
            if (tagId == -1) {
                return tagDao.save(tag);
            }
        }
        return tagId;
    }

    public Tag getTagById(int tagId) {
        return tagDao.get(tagId);
    }

    public List<Tag> getAllTags() {
        return tagDao.getAll();
    }

    public void updateTag(Tag tag) {
        tagDao.update(tag);
    }

    public void deleteTag(Tag tag) {
        tagDao.deleteById(tag.getId());
    }
}
