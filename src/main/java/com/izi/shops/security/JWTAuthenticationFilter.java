package com.izi.shops.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.izi.shops.entities.AppUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            AppUser creds = new ObjectMapper().readValue(req.getInputStream(),
                    AppUser.class);

            return authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(creds
                            .getEmail(), creds.getPassword(),
                            new ArrayList<>()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        User user = (User) authResult.getPrincipal() ;
        String Token = Jwts.builder()
                .setSubject( user.getUsername())
                .setExpiration( new Date( System.currentTimeMillis()+ SecurityConstants.EXPIRATIONTIME )  )
                .signWith( SignatureAlgorithm.HS512 , SecurityConstants.SECRET )
                .claim("roles", new ArrayList<>())
                .compact();

        response.addHeader(SecurityConstants.HEADER_STRING , SecurityConstants.TOKEN_PREFIX+Token);
        response.addHeader(SecurityConstants.ROLE, new ArrayList<>().toString());
    }

}
