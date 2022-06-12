package com.elearning.system.repositories.dao.implementation;

import com.elearning.system.repositories.dao.TokenDao;
import com.elearning.system.repositories.entities.Token;
import com.elearning.system.repositories.entities.enums.TokenType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@PropertySource("classpath:system.properties")
public class TokenDaoImpl implements TokenDao {

    @Value("#{${sql.tokens}}")
    private Map<String, String> tokensQueries;

    @Autowired
    @Qualifier("postgresJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Override
    public Token get(int id) {
        String selectQuery = tokensQueries.get("getByUserId");
        Token token;
        try {
            token = jdbcTemplate.queryForObject(selectQuery,
                    new Object[]{id},
                    new TokenRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return token;
    }

    @Override
    public int getUserId(Token token) {
        String userIdQuery = tokensQueries.get("getUserId");
        int id;
        try {
            id = jdbcTemplate.queryForObject(userIdQuery,
                    new Object[]{token.getToken(),
                            token.getTokenType().name().toLowerCase()},
                    Integer.class);
        } catch (EmptyResultDataAccessException | NullPointerException e) {
            return 0;
        }

        return id;
    }

    @Override
    public void deleteOldTokens() {
        jdbcTemplate.update(tokensQueries.get("deleteOld"));
    }

    @Override
    public List<Token> getAll() {
        return null;
    }

    @Override
    public int save(Token token) {
        String saveQuery = tokensQueries.get("saveToken");

        try {
            jdbcTemplate.update(saveQuery,
                    token.getToken(), token.getTokenType().name().toLowerCase(), token.getUserId());
        } catch (Exception e) {
            return -1;
        }
        return 0;

    }

    @Override
    public void update(Token token) {

    }

    @Override
    public void deleteById(int id) {

    }

    private class TokenRowMapper implements RowMapper<Token> {

        @Override
        public Token mapRow(ResultSet resultSet, int i) throws SQLException {
            return Token.builder()
                    .token(resultSet.getString("token"))
                    .tokenType(TokenType.valueOf(resultSet.getString("token_type").toUpperCase()))
                    .expiredDate(resultSet.getDate("expired_date"))
                    .userId(resultSet.getInt("user_id"))
                    .build();
        }
    }
}
