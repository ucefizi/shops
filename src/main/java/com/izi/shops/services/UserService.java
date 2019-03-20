package com.izi.shops.services;

import com.izi.shops.entities.AppUser;
import com.izi.shops.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;

@Service
public class UserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AppUser createUser(AppUser user) throws AuthenticationException {
        AppUser existingAppUser = appUserRepository.findByEmail(user.getEmail());
        if (existingAppUser != null) throw new AuthenticationException("User Already exists");
        AppUser newUser = new AppUser();
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        return appUserRepository.save(newUser);
    }
}
