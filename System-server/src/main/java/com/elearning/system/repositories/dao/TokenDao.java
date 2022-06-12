package com.elearning.system.repositories.dao;

import com.elearning.system.repositories.entities.Token;

public interface TokenDao extends GenericDao<Token> {
    int getUserId(Token token);

    void deleteOldTokens();
}
