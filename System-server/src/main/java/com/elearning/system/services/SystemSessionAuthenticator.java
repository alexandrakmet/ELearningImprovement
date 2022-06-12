package com.elearning.system.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

@Component("sessionAuthenticator")
@PropertySource("classpath:mail/application-mail-config.properties")
public class SystemSessionAuthenticator extends Authenticator {

    @Value("${login}")
    private String login;

    @Value("${password}")
    private String password;

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(login, password);
    }
}
