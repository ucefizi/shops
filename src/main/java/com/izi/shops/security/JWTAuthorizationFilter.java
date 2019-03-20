package com.izi.shops.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class JWTAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chaine)
            throws ServletException, IOException {

        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, "
                +"Content-Type, Access-Control-Request-Method, "
                +"Access-Control-Request-Headers,Authorization"
        );
        res.setHeader("Access-Control-Allow-Methods",
                "POST,GET,OPTIONS,DELETE,PUT");
        res.setHeader("Access-Control-Expose-Headers" ,"Access-Control-Allow-Origin,"
                + "Access-Control-Allow-Credentials,"
                + "Authorization");

        if( req.getMethod().equals("OPTIONS") ) {
            res.setStatus( HttpServletResponse.SC_OK );
            return ;
        }

        String token = req.getHeader(SecurityConstants.HEADER_STRING);
        if (token == null || !token.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            chaine.doFilter(req, res);
            return;
        }

        Claims claims = Jwts.parser().setSigningKey(SecurityConstants.SECRET)
                .parseClaimsJws(token.replace(SecurityConstants.TOKEN_PREFIX, "")).getBody();

        String username = claims.getSubject();
        ArrayList<Map<String, String>> roles = (ArrayList<Map<String, String>>) claims.get("roles");
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (Map<String, String> map : roles) {
            authorities.add(new SimpleGrantedAuthority(map.get("authority")));
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
                null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chaine.doFilter(req, res);
    }

}
