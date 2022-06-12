package com.elearning.system.repositories.dao.mappers.extractors;

import com.elearning.system.repositories.entities.Announcement;
import com.elearning.system.repositories.entities.Image;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class AnnouncementExtractor implements ResultSetExtractor<List<Announcement>> {
    @Override
    public List<Announcement> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<Announcement> announcements = new ArrayList<>();

        while (resultSet.next()) {
            Announcement announcement = Announcement.builder()
                    .id(resultSet.getInt("id"))
                    .authorId(resultSet.getInt("author_id"))
                    .authorLogin(resultSet.getString("author_login"))
                    .isPublished(resultSet.getBoolean("is_published"))
                    .title(resultSet.getString("title"))
                    .subtitle(resultSet.getString("subtitle"))
                    .fullText(resultSet.getString("full_text"))
                    .createdDate(resultSet.getTimestamp("created_date"))
                    .imageId(resultSet.getInt("image_id"))
                    .image(
                            Image.builder()
                                    .id(resultSet.getInt("image_id"))
                                    .src(resultSet.getString("src"))
                                    .build()
                    )
                    .build();
            announcements.add(announcement);
        }

        return announcements;
    }
}
