package com.elearning.system.repositories.entities.enums;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

import static java.util.Arrays.asList;

public enum MessageInfo {
    registration(
            new MessageInfoItem(Lang.UA, "mail/registration-ua.html", "Підтвердження реєстрації на ELearning"),
            new MessageInfoItem(Lang.EN, "mail/registration-en.html", "Confirm registration on ELearning")
    ),
    passwordRecover(
            new MessageInfoItem(Lang.UA, "mail/passwordRecovery-ua.html", "Скидання паролю на ELearning"),
            new MessageInfoItem(Lang.EN, "mail/passwordRecovery-en.html", "Confirm reset password on ELearning")
    ),
    suggestion(
            new MessageInfoItem(Lang.UA, "mail/suggestions-ua.html", "Пропозиції від ELearning"),
            new MessageInfoItem(Lang.EN, "mail/suggestions-en.html", "Suggestions from ELearning")
    ),
    deactivationAccount(
            new MessageInfoItem(Lang.UA, "mail/deactivationAccount-ua.html", "Ваш акаунт на ELearning деактивовано"),
            new MessageInfoItem(Lang.EN, "mail/deactivationAccount-en.html", "Your account on ELearning was deactivated")
    ),
    activationAccount(
            new MessageInfoItem(Lang.UA, "mail/activationAccount-ua.html", "Ваш акаунт на ELearning активовано"),
            new MessageInfoItem(Lang.EN, "mail/activationAccount-en.html", "Your account on ELearning was activated")
    );


    private final List<MessageInfoItem> items;

    MessageInfo(MessageInfoItem... items) {
        this.items = asList(items);
    }

    public MessageInfoItem findByLang(Lang lang) {
        return items
                .stream()
                .filter(item -> item.getLang().equals(lang))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Failed to find template by lang: " + lang.name()));
    }

    @Data
    @AllArgsConstructor
    public static class MessageInfoItem {
        private Lang lang;
        private final String filename;
        private final String subject;
    }
}
