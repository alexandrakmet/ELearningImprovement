package com.elearning.system.repositories.dao.implementation;

import com.elearning.system.repositories.dao.SuggestionDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
@PropertySource("classpath:suggestions.properties")
public class SuggestionDaoImpl implements SuggestionDao {
    @Value("#{${sql.suggestions}}")
    private Map<String, String> suggestionsQueries;

    @Autowired
    @Qualifier("postgresJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Override
    public Map<String, String> getLoginAndEmail(int quizId) {
        return jdbcTemplate.query(suggestionsQueries.get("getEmail"),
                new Object[]{quizId, quizId},
                (ResultSet rs) -> {
            Map<String, String> results = new HashMap<>();
            while (rs.next()) {
                results.put(rs.getString("login"),
                        rs.getString("email"));
            }
            return results;
        });
    }
}
