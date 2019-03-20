package com.izi.shops.resources;

import com.izi.shops.entities.AppUser;
import com.izi.shops.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.naming.AuthenticationException;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserResourceTest {

    @Mock private UserService userService;
    @InjectMocks private UserResource userResource;

    private AppUser appUser;

    @Before
    public void setup() {
        appUser = new AppUser();
        appUser.setEmail("email");
        appUser.setPassword("pwd");
    }

    @Test
    public void createUserSuccess() throws AuthenticationException {
        when(userService.createUser(any(AppUser.class))).thenReturn(appUser);
        assertEquals(appUser, userResource.createUser(appUser).getBody());
    }

    @Test()
    public void creatUserFailure() throws AuthenticationException{
        when(userService.createUser(any(AppUser.class))).thenThrow(new AuthenticationException("User exists"));
        assertEquals("A username with that email address already exists", userResource.createUser(appUser).getBody());
    }
}