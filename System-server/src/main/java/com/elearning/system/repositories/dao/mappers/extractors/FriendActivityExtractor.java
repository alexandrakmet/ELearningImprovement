package com.elearning.system.repositories.dao.mappers.extractors;

import com.elearning.system.repositories.entities.FriendActivity;
import com.elearning.system.repositories.entities.enums.FriendActivityType;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FriendActivityExtractor implements ResultSetExtractor<List<FriendActivity>> {
    @Override
    public List<FriendActivity> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<FriendActivity> activities = new ArrayList<>();

        while (resultSet.next()) {
            String type = resultSet.getString("type");
            FriendActivityType activityType;
            switch (type) {
                case "friend":
                    activityType = FriendActivityType.ADD_FRIEND;
                    break;
                case "mark":
                    activityType = FriendActivityType.MARK_AS_FAVORITE;
                    break;
                case "quiz":
                    activityType = FriendActivityType.PUBLISH_QUIZ;
                    break;
                case "achievement":
                    activityType = FriendActivityType.ACHIEVEMENT;
                    break;
                default:
                    activityType = FriendActivityType.UNDEFINED;
                    break;
            }

            FriendActivity activity = FriendActivity.builder()
                    .friendId(resultSet.getInt("id"))
                    .friendLogin(resultSet.getString("login"))
                    .friendImageSrc(resultSet.getString("src"))
                    .activityId(resultSet.getInt("activity_id"))
                    .activityContent(resultSet.getString("activity_content"))
                    .activityDate(resultSet.getTimestamp("date"))
                    .type(activityType)
                    .build();
            activities.add(activity);
        }

        return activities;
    }
}
