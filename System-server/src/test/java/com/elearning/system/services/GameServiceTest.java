package com.elearning.system.services;

import com.elearning.system.repositories.dao.GameDao;
import com.elearning.system.repositories.dto.game.UserDto;
import com.elearning.system.repositories.entities.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GameServiceTest {

    private final String GAME_ID = "123456789";
    @InjectMocks
    private GameService gameService;
    @Mock
    private GameDao gameDao;
    @Mock
    private UserService userService;
    @Mock
    private WebSocketSenderService socketSenderService;
    private List<UserDto> connectedUsers = new ArrayList<>();

    @Before
    public void init() {
        connectedUsers.add(UserDto.builder().gameId(GAME_ID).registerId(1).build());
        connectedUsers.add(UserDto.builder().gameId(GAME_ID).registerId(2).build());
    }

    @Test
    public void connectUserTestForNewRegisteredPlayer() {
        User newUser = User.builder().id(3).login("newUser").build();
        UserDto newUserDto = UserDto.builder()
                .id(1)
                .login(newUser.getLogin())
                .registerId(newUser.getId())
                .gameId(GAME_ID)
                .build();

        when(gameDao.getUsersByGame(GAME_ID)).thenReturn(connectedUsers);
        when(userService.getUserDataById(newUser.getId())).thenReturn(newUser);
        when(gameDao.saveUser(any(UserDto.class))).thenReturn(1);
        doNothing().when(socketSenderService).sendUsers(any(String.class), anyList());

        assertEquals(newUserDto, gameService.connectUser(GAME_ID, newUser.getId()));
    }

    @Test
    public void connectUserTestForConnectedPlayer() {
        User newUser = User.builder().id(3).login("newUser").build();
        UserDto newUserDto = UserDto.builder()
                .id(1)
                .login(newUser.getLogin())
                .registerId(newUser.getId())
                .gameId(GAME_ID)
                .build();

        when(gameDao.getUsersByGame(GAME_ID)).thenReturn(connectedUsers);
        when(userService.getUserDataById(newUser.getId())).thenReturn(newUser);
        when(gameDao.saveUser(any(UserDto.class))).thenReturn(1);
        doNothing().when(socketSenderService).sendUsers(any(String.class), anyList());

        assertEquals(newUserDto, gameService.connectUser(GAME_ID, newUser.getId()));

        verify(gameDao, times(2)).getUsersByGame(GAME_ID);
        verify(gameDao).saveUser(any());
        verify(userService).getUserDataById(newUser.getId());
        verify(socketSenderService).sendUsers(any(String.class), anyList());
    }
}
