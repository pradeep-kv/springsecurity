package com.pradeep.springsecurity.util;

import com.pradeep.springsecurity.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    final JwtConfig jwtConfig;

    public JwtUtil(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public String generateToken(Authentication authentication){
        return createToken(authentication.getName(), authentication.getAuthorities());
    }

    public String createToken(String username, Collection<? extends GrantedAuthority> authorities){
        return Jwts.builder()
                .setSubject(username)
                .claim("authorities", authorities)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getAccessTokenValidity() * 1000))
                .signWith(jwtConfig.secretKey())
                .compact();
    }

    public Authentication verifyToken(String token) throws BadCredentialsException {
        Authentication authentication;
        if(Strings.isNotBlank(token) && Strings.isNotEmpty(token)){
            Claims claims = authenticateToken(token);
            authentication = getAuthenticationFromClaims(claims);
        }else{
            throw new BadCredentialsException("Token is not present");
        }
        return authentication;
    }

    private Claims authenticateToken(String token) throws BadCredentialsException {
        try{

            return Jwts
                    .parser()
                    .setSigningKey(jwtConfig.secretKey())
                    .parseClaimsJws(token)
                    .getBody();
        }catch (JwtException jwtException){
            throw new BadCredentialsException("Could not parse token -" + token);
        }
    }

    private Authentication getAuthenticationFromClaims(Claims claims) {
        Authentication authentication;
        String username = claims.getSubject();
        List<Map<String, String>> authorities = (List<Map<String, String>>) claims.get("authorities");

        Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
                .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                .collect(Collectors.toSet());

        authentication = new UsernamePasswordAuthenticationToken(username,null,simpleGrantedAuthorities);
        return  authentication;
    }
}
