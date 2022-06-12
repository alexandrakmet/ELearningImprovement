package com.elearning.system.repositories.dao.implementation;

import com.elearning.system.repositories.dao.GenericDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
public abstract class GenericDaoImpl<T> implements GenericDao<T> {

    @Value("#{${sql.generic}}")
    private Map<String, String> genericQueries;

    @Autowired
    @Qualifier("postgresJdbcTemplate")
    protected JdbcTemplate jdbcTemplate;

    private final String tableName;
    private final RowMapper<T> rowMapper;

    protected GenericDaoImpl(RowMapper<T> rowMapper, String tableName) {
        this.rowMapper = rowMapper;
        this.tableName = tableName;
    }

    @Override
    public List<T> getAll() {
        return jdbcTemplate.query(String.format(genericQueries.get("getAll"), tableName), rowMapper);
    }

    @Override
    public T get(int id) {
        T object;
        try {
            object = jdbcTemplate.queryForObject(String.format(genericQueries.get("get"), tableName),
                    new Object[]{id}, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            log.info("No object with id {} int table {}", id, tableName);
            return null;
        }
        return object;
    }

    protected abstract String getInsertQuery();

    protected abstract PreparedStatement getInsertPreparedStatement(PreparedStatement preparedStatement, T t) throws SQLException;

    @Override
    public int save(T t) {
        String insertQuery = getInsertQuery();
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection
                        .prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
                return getInsertPreparedStatement(preparedStatement, t);
            }, keyHolder);
        } catch (DuplicateKeyException e) {
            log.info("DuplicateKey while inserting object {}", t);
            return -1;
        }
        return (int) Objects.requireNonNull(keyHolder.getKeys()).get("id");
    }

    protected abstract String getUpdateQuery();

    protected abstract Object[] getUpdateParameters(T t);

    @Override
    public void update(T t) {
        jdbcTemplate.update(getUpdateQuery(), getUpdateParameters(t));
    }

    @Override
    public void deleteById(int id) {
        jdbcTemplate.update(String.format(genericQueries.get("delete"), tableName), id);
    }

}
