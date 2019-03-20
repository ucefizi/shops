package com.izi.shops.resources;

import com.izi.shops.entities.AppUser;
import com.izi.shops.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping
public class UserResource {

    private final UserService userService;

    @Autowired
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity createUser(@RequestBody AppUser user) {
        try {
            return ResponseEntity.ok(userService.createUser(user));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(400).body("A username with that email address already exists");
        }
    }
}
