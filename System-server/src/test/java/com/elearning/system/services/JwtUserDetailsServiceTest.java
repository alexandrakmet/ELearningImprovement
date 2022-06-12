package com.elearning.system.services;

import com.elearning.system.repositories.dao.UserDao;
import com.elearning.system.repositories.entities.User;
import com.elearning.system.repositories.entities.enums.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JwtUserDetailsServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private JwtUserDetailsService mockService;


    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsernameThrowsUsernameNotFoundException() {

        final String TEST_USER_LOGIN = "userLogin";

        when(userDao.getUserByLogin(anyString())).thenReturn(null);

        mockService.loadUserByUsername(TEST_USER_LOGIN);

    }

    @Test
    public void loadUserByUsernameReturnsUserDetails() {

        final String TEST_USER_LOGIN = "userLogin";
        final String TEST_USER_PASSWORD = "userPassword";
        final Role TEST_USER_ROLE = Role.USER;

        final UserDetails TEST_USER_DETAILS = new org.springframework.security.core.userdetails.User(
                TEST_USER_LOGIN,
                TEST_USER_PASSWORD,
                List.of(new SimpleGrantedAuthority("ROLE_" + TEST_USER_ROLE)));

        final User TEST_USER = User.builder()
                .login(TEST_USER_LOGIN)
                .password(TEST_USER_PASSWORD)
                .role(TEST_USER_ROLE)
                .build();

        when(userDao.getUserByLogin(anyString())).thenReturn(TEST_USER);

        UserDetails result = mockService.loadUserByUsername(TEST_USER_LOGIN);

        assertEquals(TEST_USER_DETAILS, result);

    }

}