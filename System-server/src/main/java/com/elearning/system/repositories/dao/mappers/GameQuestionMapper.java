package com.elearning.system.repositories.dao.mappers;

import com.elearning.system.repositories.dto.game.GameQuestionDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GameQuestionMapper implements RowMapper<GameQuestionDto> {
    @Override
    public GameQuestionDto mapRow(ResultSet resultSet, int i) throws SQLException {
        return GameQuestionDto.builder()
                .id(resultSet.getInt("id"))
                .gameId(resultSet.getString("game_id"))
                .questionId(resultSet.getInt("question_id"))
                .isCurrent(resultSet.getBoolean("is_current"))
                .finishTime(resultSet.getTimestamp("finish_time"))
                .build();
    }
}
