package com.elearning.system.repositories.dao.implementation;

import com.elearning.system.repositories.dao.mappers.extractors.FriendActivityExtractor;
import com.elearning.system.repositories.dao.UserDao;
import com.elearning.system.repositories.dao.mappers.UserMapper;
import com.elearning.system.repositories.entities.FriendActivity;
import com.elearning.system.repositories.entities.User;
import com.elearning.system.repositories.entities.enums.Lang;
import com.elearning.system.repositories.entities.enums.Role;
import com.elearning.system.repositories.entities.enums.UserAccountStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@PropertySource("classpath:system.properties")
public class UserDaoImpl extends GenericDaoImpl<User> implements UserDao {

    @Value("#{${sql.users}}")
    private Map<String, String> usersQueries;

    @Value("#{${sql.friends}}")
    private Map<String, String> friendsQueries;

    @Value("#{${sql.friendsActivity}}")
    private Map<String, String> friendsActivityQueries;

    protected UserDaoImpl() {
        super(new UserMapper(), TABLE_NAME);
    }

    @Override
    protected String getInsertQuery() {
        return usersQueries.get("insert");
    }

    @Override
    protected PreparedStatement getInsertPreparedStatement(PreparedStatement preparedStatement,
                                                           User user) throws SQLException {
        preparedStatement.setString(1, user.getLogin());
        preparedStatement.setString(2, user.getPassword());
        preparedStatement.setString(3, user.getMail());
        preparedStatement.setString(4,
                user.getStatus() != null
                        ? user.getStatus().name().toLowerCase()
                        : UserAccountStatus.UNACTIVATED.name().toLowerCase());
        preparedStatement.setString(5,
                user.getRole() != null
                        ? user.getRole().name().toLowerCase()
                        : Role.USER.name().toLowerCase());
        preparedStatement.setString(6, user.getFirstName());
        preparedStatement.setString(7, user.getSecondName());
        if (user.getProfile() != null) {
            preparedStatement.setString(8, user.getProfile());
        } else {
            preparedStatement.setString(8, "");
        }
        preparedStatement.setInt(9, user.getScore());
        preparedStatement.setInt(10, user.getImageId());
        return preparedStatement;
    }

    @Override
    protected String getUpdateQuery() {
        return usersQueries.get("update");
    }

    @Override
    protected Object[] getUpdateParameters(User user) {
        return new Object[]{user.getLogin(),
                user.getPassword(),
                user.getMail(),
                user.getStatus().name().toLowerCase(),
                user.getRole().name().toLowerCase(),
                user.getFirstName(),
                user.getSecondName(),
                user.getRegistrationDate(),
                user.getProfile(),
                user.getScore(),
                user.getImageId(),
                user.getId()};
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(usersQueries.get("getAllUsers"), new UserMapper());
    }

    @Override
    public User get(int id) {
        User user;
        try {
            user = jdbcTemplate.queryForObject(usersQueries.get("getUser"),
                    new Object[]{id},
                    new UserMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return user;
    }

    @Override
    public User getUserByLoginAndPassword(String login, String password) {
        User user;
        try {
            user = jdbcTemplate.queryForObject(usersQueries.get("selectByLoginAndPassword"),
                    new Object[]{login, password},
                    new UserMapper()
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return user;
    }

    @Override
    public User getUserByMail(String mail) {
        User user;
        try {
            user = jdbcTemplate.queryForObject(usersQueries.get("selectByMail"),
                    new Object[]{mail},
                    new UserMapper());
        } catch (NullPointerException | EmptyResultDataAccessException e) {
            return null;
        }
        return user;
    }

    @Override
    public Page<User> getUserByRole(Role role, Pageable pageable) {
        Number rowTotal = jdbcTemplate.queryForObject(usersQueries.get("rowCount"),
                new Object[]{role.name().toLowerCase()},
                (resultSet, number) -> resultSet.getInt(1));
        List<User> users = jdbcTemplate.query(usersQueries.get("getPageByRole"),
                new Object[]{role.name().toLowerCase(), pageable.getPageSize(), pageable.getOffset()},
                new UserMapper());
        return new PageImpl<>(users, pageable, rowTotal != null ? rowTotal.intValue() : 0);
    }

    @Override
    public Page<User> getAllUsersPage(Pageable pageable) {
        Number total = jdbcTemplate.queryForObject(usersQueries.get("allUsersCount"),
                new Object[]{},
                (resultSet, number) -> resultSet.getInt(1));
        List<User> users = jdbcTemplate.query(
                usersQueries.get("getAllUsersPage"),
                new Object[]{pageable.getPageSize(), pageable.getOffset()},
                new UserMapper());
        return new PageImpl<>(users, pageable, total != null ? total.intValue() : 0);
    }

    @Override
    public User getUserByLogin(String login) {
        User user;
        try {
            user = jdbcTemplate.queryForObject(usersQueries.get("selectByLogin"),
                    new Object[]{login}, new UserMapper()
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return user;
    }

    @Override
    public boolean addUserFriend(int userId, int friendId) {
        try {
            jdbcTemplate.update(
                    friendsQueries.get("addUserFriend"),
                    userId, friendId
            );
        } catch (DuplicateKeyException e) {
            return false;
        }
        return true;
    }

    @Override
    public void deleteUserFriend(int userId, int friendId) {
        jdbcTemplate.update(
                friendsQueries.get("deleteUserFriend"),
                userId, friendId
        );
    }

    @Override
    public List<User> getUserFriends(int userId) {
        return jdbcTemplate.query(
                friendsQueries.get("getUserFriends"),
                new Object[]{userId}, new UserMapper()
        );
    }

    @Override
    public Page<User> getUserFriendsPage(int userId, Pageable pageable) {
        Number total = jdbcTemplate.queryForObject(friendsQueries.get("rowCount"),
                new Object[]{userId},
                (resultSet, number) -> resultSet.getInt("row_count"));

        List<User> friends = jdbcTemplate.query(
                friendsQueries.get("getUserFriends").replace(";", " LIMIT ? OFFSET ?;"),
                new Object[]{userId, pageable.getPageSize(), pageable.getOffset()},
                new UserMapper());
        return new PageImpl<>(friends, pageable, total != null ? total.intValue() : 0);
    }

    @Override
    public List<FriendActivity> getAllFriendsActivity(int userId) {
        return jdbcTemplate.query(
                friendsActivityQueries.get("getAllFriendsActivity"),
                new Object[]{userId}, new FriendActivityExtractor()
        );
    }

    @Override
    public Page<FriendActivity> getAllFriendsActivityPage(int userId, Pageable pageable) {
        Number total = jdbcTemplate.queryForObject(friendsActivityQueries.get("allRowCount"),
                new Object[]{userId},
                (resultSet, number) -> resultSet.getInt("row_count"));

        List<FriendActivity> activities = jdbcTemplate.query(
                friendsActivityQueries.get("getAllFriendsActivity").replace(";", " LIMIT ? OFFSET ?;"),
                new Object[]{userId, pageable.getPageSize(), pageable.getOffset()},
                new FriendActivityExtractor());
        return new PageImpl<>(activities != null ? activities : new ArrayList<>(), pageable, total != null ? total.intValue() : 0);
    }

    @Override
    public List<FriendActivity> getFilteredFriendsActivity(int userId, boolean addFriend, boolean markQuizAsFavorite,
                                                           boolean publishQuiz, boolean achievement) {
        String query = buildActivityFilterQuery(addFriend, markQuizAsFavorite, publishQuiz, achievement);
        query = friendsActivityQueries.get("activitySelectStart") + query + friendsActivityQueries.get("activitySelectEnd");
        return jdbcTemplate.query(query,
                new Object[]{userId}, new FriendActivityExtractor()
        );
    }

    @Override
    public Page<FriendActivity> getFilteredFriendsActivityPage(int userId, boolean addFriend, boolean markQuizAsFavorite,
                                                               boolean publishQuiz, boolean achievement, Pageable pageable) {
        String innerQuery = buildActivityFilterQuery(addFriend, markQuizAsFavorite, publishQuiz, achievement);
        String query = friendsActivityQueries.get("activitySelectStart") + innerQuery + friendsActivityQueries.get("activitySelectEnd");
        String countQuery = friendsActivityQueries.get("activityCountStart") + innerQuery + friendsActivityQueries.get("activityCountEnd");

        Number total = jdbcTemplate.queryForObject(countQuery,
                new Object[]{userId},
                (resultSet, number) -> resultSet.getInt("row_count"));

        List<FriendActivity> activities = jdbcTemplate.query(
                query.replace(";", " LIMIT ? OFFSET ?;"),
                new Object[]{userId, pageable.getPageSize(), pageable.getOffset()},
                new FriendActivityExtractor());
        return new PageImpl<>(activities != null ? activities : new ArrayList<>(), pageable, total != null ? total.intValue() : 0);
    }

    @Override
    public List<User> searchUsersByLogin(String login) {
        login = '%' + login + '%';
        return jdbcTemplate.query(
                usersQueries.get("searchUsersByLogin"),
                new Object[]{login}, new UserMapper()
        );
    }

    @Override
    public List<User> searchUsersByLogin(String login, Role role) {
        login = '%' + login + '%';
        return jdbcTemplate.query(
                usersQueries.get("searchUsersByLogin").replace(";", " AND role = cast(? AS user_role);"),
                new Object[]{login, role.name().toLowerCase()}, new UserMapper()
        );
    }

    @Override
    public Page<User> searchUsersByLogin(String login, Pageable pageable) {
        login = '%' + login + '%';

        Number total = jdbcTemplate.queryForObject(usersQueries.get("countRowsForSearchByLogin"),
                new Object[]{login},
                (resultSet, number) -> resultSet.getInt("row_count"));

        List<User> users = jdbcTemplate.query(
                usersQueries.get("searchUsersByLogin").replace(";", " LIMIT ? OFFSET ?;"),
                new Object[]{login, pageable.getPageSize(), pageable.getOffset()},
                new UserMapper());
        return new PageImpl<>(users, pageable, total != null ? total.intValue() : 0);
    }

    @Override
    public Page<User> searchUsersByLogin(String login, Role role, Pageable pageable) {
        login = '%' + login + '%';

        Number total = jdbcTemplate.queryForObject(usersQueries
                        .get("countRowsForSearchByLogin")
                        .replace(";", " AND role = cast(? AS user_role);"),
                new Object[]{login, role.name().toLowerCase()},
                (resultSet, number) -> resultSet.getInt("row_count"));

        List<User> users = jdbcTemplate.query(
                usersQueries.get("searchUsersByLogin").replace(";", " AND role = cast(? AS user_role) LIMIT ? OFFSET ?;"),
                new Object[]{login, role.name().toLowerCase(), pageable.getPageSize(), pageable.getOffset()},
                new UserMapper());
        return new PageImpl<>(users, pageable, total != null ? total.intValue() : 0);
    }

    @Override
    public boolean checkUsersFriendship(int firstUserId, int secondUserId) {
        Number total = jdbcTemplate.queryForObject(friendsQueries.get("checkFriendship"),
                new Object[]{firstUserId, secondUserId},
                (resultSet, number) -> resultSet.getInt("row_count"));

        if (total == null) {
            return false;
        }

        return total.intValue() > 0;
    }

    @Override
    public void updateUserStatus(int id, UserAccountStatus status) {
        jdbcTemplate.update(usersQueries.get("updateUserStatus"),
                status.name().toLowerCase(), id);
    }

    @Override
    public void updateUserScore(int userId, int score) {
        jdbcTemplate.update(usersQueries.get("updateUserScore"), score, userId);
    }

    @Override
    public Lang getUserLanguage(int userId) {
        return jdbcTemplate.queryForObject(usersQueries.get("getLanguage"),
                new Object[]{userId},
                (resultSet, number) -> Lang.valueOf(resultSet.getString("language").toUpperCase()));
    }

    @Override
    public void updateUserLanguage(int userId, Lang lang) {
        jdbcTemplate.update(usersQueries.get("updateLanguage"), lang.getCode(), userId);
    }

    private String buildActivityFilterQuery(boolean addFriend, boolean markQuizAsFavorite,
                                            boolean publishQuiz, boolean achievement) {
        String query = "";
        boolean isUnion = false;

        if (addFriend) {
            if (isUnion) {
                query += " UNION ALL ";
            }
            isUnion = true;
            query += friendsActivityQueries.get("addFriendPart");
        }

        if (markQuizAsFavorite) {
            if (isUnion) {
                query += " UNION ALL ";
            }
            isUnion = true;
            query += friendsActivityQueries.get("markQuizPart");
        }

        if (publishQuiz) {
            if (isUnion) {
                query += " UNION ALL ";
            }
            isUnion = true;
            query += friendsActivityQueries.get("publishQuizPart");
        }

        if (achievement) {
            if (isUnion) {
                query += " UNION ALL ";
            }
            query += friendsActivityQueries.get("achievementPart");
        }

        return query;
    }

    @Override
    public void updateUserPhoto(int imageId, int userId) {
        jdbcTemplate.update(usersQueries.get("updateUserPhoto"),
                imageId, userId);
    }

    @Override
    public void changePassword(String password, String login) {
        jdbcTemplate.update(usersQueries.get("changePassword"),
                password, login);
    }
}
