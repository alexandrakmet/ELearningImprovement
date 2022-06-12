package com.elearning.system.repositories.dao.implementation;

import com.elearning.system.repositories.dao.TagDao;
import com.elearning.system.repositories.dao.mappers.TagMapper;
import com.elearning.system.repositories.entities.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@PropertySource("classpath:system.properties")
public class TagDaoImpl extends GenericDaoImpl<Tag> implements TagDao {
    @Value("#{${sql.tag}}")
    private Map<String, String> tagQueries;

    protected TagDaoImpl() {
        super(new TagMapper(), TABLE_NAME);
    }

    @Override
    protected String getInsertQuery() {
        return tagQueries.get("insert");
    }

    @Override
    protected PreparedStatement getInsertPreparedStatement(PreparedStatement preparedStatement,
                                                           Tag tag) throws SQLException {
        preparedStatement.setString(1, tag.getName());
        return preparedStatement;
    }

    @Override
    protected String getUpdateQuery() {
        return tagQueries.get("update");
    }

    @Override
    protected Object[] getUpdateParameters(Tag tag) {
        return new Object[]{tag.getName(),
                tag.getId()};
    }

    @Override
    public List<Tag> getByQuizId(int id) {
        return jdbcTemplate.query(tagQueries.get("getByQuizId"),
                new Object[]{id},
                new TagMapper());
    }

    @Override
    public int getIdByName(String name) {
        Number id;
        try {
            id = jdbcTemplate.queryForObject(tagQueries.get("getIdByName"),
                    new Object[]{name}, Integer.class);
        } catch (EmptyResultDataAccessException | NullPointerException e) {
            return -1;
        }
        return id != null ? id.intValue() : -1;
    }
}
