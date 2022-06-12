package com.elearning.system.services;

import com.elearning.system.repositories.dao.TokenDao;
import com.elearning.system.repositories.entities.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TokenService {


    @Autowired
    private TokenDao tokenDao;

    @Scheduled(cron = "0 0 * * * *")
    public void deleteOldTokens() {
        tokenDao.deleteOldTokens();
    }

    void saveToken(Token tokenForNewUser) {
        tokenDao.save(tokenForNewUser);
    }

    public int getUserId(Token token) {
        return tokenDao.getUserId(token);
    }

    Token getTokenByUserId(int id) {
        return tokenDao.get(id);
    }

}
