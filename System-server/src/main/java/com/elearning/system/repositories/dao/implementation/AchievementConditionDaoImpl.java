package com.elearning.system.repositories.dao.implementation;

import com.elearning.system.repositories.dao.AchievementConditionDao;
import com.elearning.system.repositories.dao.mappers.AchievementConditionMapper;
import com.elearning.system.repositories.entities.AchievementCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@PropertySource("classpath:achievement.properties")
public class AchievementConditionDaoImpl extends GenericDaoImpl<AchievementCondition> implements AchievementConditionDao {

    @Value("#{${sql.achievementCondition}}")
    private Map<String, String> achievementConditionQueries;

    protected AchievementConditionDaoImpl() {
        super(new AchievementConditionMapper(), TABLE_NAME);
    }

    @Override
    protected String getInsertQuery() {
        return achievementConditionQueries.get("insert");
    }

    @Override
    protected PreparedStatement getInsertPreparedStatement(PreparedStatement preparedStatement,
                                                           AchievementCondition achievementCondition) throws SQLException {
        preparedStatement.setString(1, achievementCondition.getOperator().name().toLowerCase());
        preparedStatement.setInt(2, achievementCondition.getValue());
        preparedStatement.setInt(3, achievementCondition.getAchievementId());
        preparedStatement.setInt(4, achievementCondition.getCharacteristicId());
        return preparedStatement;
    }

    @Override
    protected String getUpdateQuery() {
        return achievementConditionQueries.get("update");
    }

    @Override
    protected Object[] getUpdateParameters(AchievementCondition achievementCondition) {
        return new Object[]{achievementCondition.getOperator().name().toLowerCase(),
                achievementCondition.getValue(),
                achievementCondition.getAchievementId(),
                achievementCondition.getCharacteristicId(),
                achievementCondition.getId()};
    }


    @Override
    public void delete(List<Integer> achievementConditions) {
        String sql = achievementConditionQueries.get("deleteAchievementConditions");
        List<String> mark = new ArrayList<>();
        for (int i = 0; i < achievementConditions.size(); i++) {
            mark.add("?");
        }
        sql = String.format(sql, String.join(", ", mark));
        jdbcTemplate.update(sql, achievementConditions.toArray());
    }

    private String getQueryForInsert(List<AchievementCondition> userAchievements) {
        String query = achievementConditionQueries.get("insertAchievementConditions");
        String insertQuery = "(cast(? AS condition_operator), ?, ?, ?)";
        for (int i = 0; i < userAchievements.size() - 1; i++) {
            query = query.concat(insertQuery + ", ");
        }
        return query.concat(insertQuery);
    }

    private Object[] getParamsToInsertQuestions(List<AchievementCondition> achievementConditions) {
        List<Object> params = new ArrayList<>();
        for (AchievementCondition achievementCondition : achievementConditions) {
            params.add(achievementCondition.getOperator().name().toLowerCase());
            params.add(achievementCondition.getValue());
            params.add(achievementCondition.getAchievementId());
            params.add(achievementCondition.getCharacteristicId());
        }
        return params.toArray();
    }

    @Override
    public void insert(List<AchievementCondition> achievementConditions) {
        jdbcTemplate.update(getQueryForInsert(achievementConditions),
                getParamsToInsertQuestions(achievementConditions));
    }
}
