package com.elearning.system.repositories.dao.implementation;

import com.elearning.system.repositories.dao.UserAchievementsDao;
import com.elearning.system.repositories.dao.mappers.AchievementMapper;
import com.elearning.system.repositories.dao.mappers.UserAchievementMapper;
import com.elearning.system.repositories.entities.Achievement;
import com.elearning.system.repositories.entities.UserAchievement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
@Repository
@PropertySource("classpath:achievement.properties")
public class UserAchievementsDaoImpl extends GenericDaoImpl<UserAchievement> implements UserAchievementsDao {

    private final String insertQuery = "(?, ?, NOW())";
    @Value("#{${sql.userAchievements}}")
    private Map<String, String> userAchievementsQueries;

    protected UserAchievementsDaoImpl() {
        super(new UserAchievementMapper(), TABLE_NAME);
    }

    @Override
    protected String getInsertQuery() {
        return userAchievementsQueries.get("insert");
    }

    @Override
    protected PreparedStatement getInsertPreparedStatement(PreparedStatement preparedStatement,
                                                           UserAchievement userAchievement) throws SQLException {
        preparedStatement.setInt(1, userAchievement.getUserId());
        preparedStatement.setInt(2, userAchievement.getAchievementId());
        return preparedStatement;
    }

    @Override
    protected String getUpdateQuery() {
        return null;
    }

    @Override
    protected Object[] getUpdateParameters(UserAchievement userAchievement) {
        return new Object[0];
    }


    @Override
    public List<Achievement> getAchievementsByUserId(int userId) {
        return jdbcTemplate.query(userAchievementsQueries.get("getAchievementsByUserId"),
                new Object[]{userId},
                new AchievementMapper());
    }

    @Override
    public void delete(List<Integer> userAchievements) {
        String sql = userAchievementsQueries.get("deleteUserAchievements");
        List<String> mark = new ArrayList<>();
        for (int i = 0; i < userAchievements.size(); i++) {
            mark.add("?");
        }
        sql = String.format(sql, String.join(", ", mark));
        log.info("sql = {}", sql);
        jdbcTemplate.update(sql, userAchievements.toArray());
    }

    private String getQueryForInsert(List<UserAchievement> userAchievements) {
        String query = userAchievementsQueries.get("insertUserAchievements");
        for (int i = 0; i < userAchievements.size() - 1; i++) {
            query = query.concat(insertQuery + ", ");
        }
        return query.concat(insertQuery);
    }

    private Object[] getParamsToInsertQuestions(List<UserAchievement> userAchievements) {
        List<Object> params = new ArrayList<>();
        for (UserAchievement userAchievement : userAchievements) {
            params.add(userAchievement.getUserId());
            params.add(userAchievement.getAchievementId());
        }
        return params.toArray();
    }

    @Override
    public void insert(List<UserAchievement> userAchievements) {
        jdbcTemplate.update(getQueryForInsert(userAchievements),
                getParamsToInsertQuestions(userAchievements));
    }


}
