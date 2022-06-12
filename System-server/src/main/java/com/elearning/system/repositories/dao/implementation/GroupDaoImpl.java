package com.elearning.system.repositories.dao.implementation;

import com.elearning.system.repositories.entities.Group;
import com.elearning.system.repositories.dao.GroupDao;
import com.elearning.system.repositories.dao.mappers.GroupMapper;
import com.elearning.system.repositories.dao.mappers.UserMapper;
import com.elearning.system.repositories.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@PropertySource("classpath:system.properties")
public class GroupDaoImpl extends GenericDaoImpl<Group> implements GroupDao {

    @Value("#{${sql.group}}")
    private Map<String, String> groupQueries;

    protected GroupDaoImpl() {
        super(new GroupMapper(), TABLE_NAME);
    }


    @Override
    protected String getInsertQuery() {
        return groupQueries.get("insert");
    }

    @Override
    protected PreparedStatement getInsertPreparedStatement(PreparedStatement preparedStatement,
                                                           Group group) throws SQLException {
        preparedStatement.setString(1, group.getName());
        preparedStatement.setInt(2, group.getAuthorId());
        preparedStatement.setString(3, group.getInvitationCode());
        return preparedStatement;
    }

    @Override
    protected String getUpdateQuery() {
        return groupQueries.get("update");
    }

    @Override
    protected Object[] getUpdateParameters(Group group) {
        return new Object[]{group.getName(), group.getId()};
    }

    @Override
    public List<Group> getUserGroups(int userId) {
        return jdbcTemplate.query(groupQueries.get("getUsersGroup"),
                new Object[]{userId, userId},
                new GroupMapper());
    }

    @Override
    public void addGroupMember(int groupId, int userId) {
        try {
            jdbcTemplate.update(groupQueries.get("addGroupMember"), groupId, userId);
        } catch (DuplicateKeyException e) {
            log.info("User " + userId + " is already a member of group " + groupId);
        }
    }

    @Override
    public List<User> getGroupMembers(int groupId) {
        return jdbcTemplate.query(groupQueries.get("getFullInfoForUser"),
                new Object[]{groupId},
                new UserMapper());
    }

    @Override
    public Group getByCode(String code) {
        List<Group> result = jdbcTemplate.query(groupQueries.get("getByCode"),
                new Object[]{code},
                new GroupMapper());
        return result.size() == 0 ? null : result.get(0);
    }

}
