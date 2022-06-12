package com.elearning.system.services;

import com.elearning.system.repositories.dao.NotificationDao;
import com.elearning.system.repositories.entities.Notification;
import com.elearning.system.repositories.entities.NotificationType;
import com.elearning.system.repositories.entities.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NotificationServiceTest {

    private final int TEST_AUTHOR_ID = 1;
    private final int TEST_OBJECT_ID = 2;
    private final int TEST_USER_ID = 3;
    private final int TEST_ID = 4;
    private final String TEST_AUTHOR_LOGIN = "TEST_AUTHOR_LOGIN";
    private final String TEST_GAME_ID = "00689482545";
    private final User TEST_USER = User.builder()
            .login(TEST_AUTHOR_LOGIN)
            .build();
    @InjectMocks
    private NotificationService mockService;
    @Mock
    private UserService userService;
    @Mock
    private NotificationDao notificationDao;

    public Notification getNotification(String TEST_ACTION, String TEST_ACTION_LINK) {

        final boolean TEST_IS_VIEWED = false;
        final String TEST_AUTHOR_LINK = "users/" + TEST_AUTHOR_ID;
        final boolean TEST_IS_MESSAGE = false;

        Notification expected = Notification.builder()
                .isViewed(TEST_IS_VIEWED)
                .author(TEST_AUTHOR_LOGIN)
                .authorLink(TEST_AUTHOR_LINK)
                .userId(TEST_USER_ID)
                .isMessage(TEST_IS_MESSAGE)
                .build();
        expected.setAction(TEST_ACTION);
        expected.setActionLink(TEST_ACTION_LINK);
        expected.setId(TEST_ID);

        return expected;
    }

    @Before
    public void initMock() {
        getNotification("CREATED_NEWS", "CREATED_NEWS");
        when(userService.getUserDataById(anyInt())).thenReturn(TEST_USER);
        when(notificationDao.save(any(Notification.class))).thenReturn(TEST_ID);
    }

    @Test
    public void generateNotificationShouldReturnNewAnnouncementNotification() {
        final String TEST_ACTION = "CREATED_NEWS";
        final String TEST_ACTION_LINK = "announcement/" + TEST_OBJECT_ID;

        Notification expected = getNotification(TEST_ACTION, TEST_ACTION_LINK);

        Notification result = mockService.generateNotification(TEST_AUTHOR_ID, TEST_OBJECT_ID, TEST_USER_ID, anyString(),
                NotificationType.CREATED_NEWS);
        assertThat(result).isEqualToComparingFieldByField(expected);
    }

    @Test
    public void generateNotificationShouldReturnNewQuizNotification() {
        final String TEST_ACTION = "CREATED_QUIZ";
        final String TEST_ACTION_LINK = "quiz/" + TEST_OBJECT_ID;

        Notification expected = getNotification(TEST_ACTION, TEST_ACTION_LINK);

        Notification result = mockService.generateNotification(TEST_AUTHOR_ID, TEST_OBJECT_ID, TEST_USER_ID, anyString(),
                NotificationType.CREATED_QUIZ);
        assertThat(result).isEqualToComparingFieldByField(expected);
    }

    @Test
    public void generateNotificationShouldReturnGameInvitationNotification() {
        final String TEST_ACTION = "GAME_INVITATION";
        final String TEST_ACTION_LINK = "game/" + TEST_GAME_ID + "/play";

        Notification expected = getNotification(TEST_ACTION, TEST_ACTION_LINK);
        expected.setUserId(TEST_AUTHOR_ID);

        Notification result = mockService.generateNotification(TEST_AUTHOR_ID, anyInt(), TEST_AUTHOR_ID, TEST_GAME_ID,
                NotificationType.GAME_INVITATION);
        assertThat(result).isEqualToComparingFieldByField(expected);
    }

    @Test
    public void generateNotificationShouldReturnFriendInvitationNotification() {
        final String TEST_ACTION = "FRIEND_INVITATION";
        final String TEST_ACTION_LINK = "users/" + TEST_AUTHOR_ID;

        Notification expected = getNotification(TEST_ACTION, TEST_ACTION_LINK);
        expected.setUserId(TEST_OBJECT_ID);
        Notification result = mockService.generateNotification(TEST_AUTHOR_ID, TEST_OBJECT_ID, TEST_USER_ID, anyString(),
                NotificationType.FRIEND_INVITATION);
        assertThat(result).isEqualToComparingFieldByField(expected);
    }

    @Test
    public void generateNotificationShouldReturnMessageNotification() {
        final String TEST_ACTION = "MESSAGE";
        final String TEST_ACTION_LINK = "chat/" + TEST_OBJECT_ID;

        Notification expected = getNotification(TEST_ACTION, TEST_ACTION_LINK);
        expected.setMessage(true);
        Notification result = mockService.generateNotification(TEST_AUTHOR_ID, TEST_OBJECT_ID, TEST_USER_ID, anyString(),
                NotificationType.MESSAGE);
        assertThat(result).isEqualToComparingFieldByField(expected);
    }
}
