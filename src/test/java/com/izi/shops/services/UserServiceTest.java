package com.izi.shops.services;

import com.izi.shops.entities.AppUser;
import com.izi.shops.repositories.AppUserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock private AppUserRepository repository;
    @Mock private PasswordEncoder passwordEncoder;
    @InjectMocks private UserService userService;

    private AppUser user;

    @Before
    public void setup() {
        user = new AppUser(1L, "email@exp.com", "abc");
        when(passwordEncoder.encode("abc")).thenReturn("123");
        when(repository.save(any(AppUser.class))).thenReturn(new AppUser(1L, "email@exp.com", "123"));
    }

    @Test
    public void createUser() {
        AppUser result = userService.createUser(user);
        AppUser expected = new AppUser(1L, "email@exp.com", "123");
        assertEquals(expected, result);
    }
}