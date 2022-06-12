package com.elearning.system.repositories.dao.implementation;

import com.elearning.system.repositories.dao.FriendListDao;
import com.elearning.system.repositories.entities.NotificationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@PropertySource("classpath:friendlist.properties")
public class FriendListDaoImpl implements FriendListDao {
    @Autowired
    @Qualifier("postgresJdbcTemplate")
    protected JdbcTemplate jdbcTemplate;
    @Value("#{${sql.friendList}}")
    private Map<String, String> friendsListQueries;

    @Override
    public boolean isSendNotification(int userId, NotificationType notificationType) {
        Optional<Boolean> isSend;
        switch (notificationType) {
            case CREATED_NEWS:
                isSend = Optional.ofNullable(jdbcTemplate.queryForObject(friendsListQueries.get("caseIsNewAnnouncement"),
                        new Object[]{userId}, boolean.class));
                break;
            case CREATED_QUIZ:
                isSend = Optional.ofNullable(jdbcTemplate.queryForObject(friendsListQueries.get("caseIsCreatedQuiz"),
                        new Object[]{userId}, boolean.class));
                break;
            case GAME_INVITATION:
                isSend = Optional.ofNullable(jdbcTemplate.queryForObject(friendsListQueries.get("caseIsGameInvitation"),
                        new Object[]{userId}, boolean.class));
                break;
            case FRIEND_INVITATION:
                isSend = Optional.ofNullable(jdbcTemplate.queryForObject(friendsListQueries.get("caseIsFriendInvitation"),
                        new Object[]{userId}, boolean.class));
                break;
            default:
                return false;
        }
        return isSend.orElse(false);
    }

    @Override
    public List<Integer> getForNotification(int userId, NotificationType notificationType) {
        List<Integer> friends;
        switch (notificationType) {
            case CREATED_NEWS:
                friends = jdbcTemplate.queryForList(friendsListQueries.get("caseNewAnnouncement"),
                        new Object[]{userId}, Integer.class);
                break;
            case CREATED_QUIZ:
                friends = jdbcTemplate.queryForList(friendsListQueries.get("caseNewQuiz"),
                        new Object[]{userId}, Integer.class);
                break;
            case GAME_INVITATION:
                friends = jdbcTemplate.queryForList(friendsListQueries.get("caseGameInvitation"),
                        new Object[]{userId}, Integer.class);
                break;
            case FRIEND_INVITATION:
                friends = jdbcTemplate.queryForList(friendsListQueries.get("caseFriendInvitation"),
                        new Object[]{userId}, Integer.class);
                break;
            case MESSAGE:
                friends = jdbcTemplate.queryForList(friendsListQueries.get("caseMessage"),
                        new Object[]{userId}, Integer.class);
                break;
            default:
                return null;
        }
        return friends;
    }
}
