package com.pradeep.springsecurity.filter;

import com.pradeep.springsecurity.config.JwtConfig;
import com.pradeep.springsecurity.util.JwtUtil;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Component
public class JwtTokenValidationFilter extends OncePerRequestFilter {
    JwtConfig jwtConfig;

    final JwtUtil jwtUtil;

    @Autowired
    public JwtTokenValidationFilter(JwtConfig jwtConfig, JwtUtil jwtUtil) {
        this.jwtConfig = jwtConfig;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
//        String requestHeader = httpServletRequest.getHeader(jwtConfig.getAuthorizationHeader());
//        After careful consideration, It was decided to keep JWT Token in Cookie
//        Below is the reference article about keeping JWT Token in Header vs Cookie
//        https://stormpath.com/blog/where-to-store-your-jwts-cookies-vs-html5-web-storage
        String token = getJwtTokenFromRequestCookie(httpServletRequest);
        if(Strings.isNotBlank(token) && Strings.isNotEmpty(token)
                && token.startsWith(jwtConfig.getTokenPrefix())){
            token = token.replace(jwtConfig.getTokenPrefix(), "");

            try{
                Authentication authentication = jwtUtil.verifyToken(token);
                if(authentication != null && SecurityContextHolder.getContext().getAuthentication() == null){
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }else {
                    throw new BadCredentialsException("Invalid Token");
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
                throw new BadCredentialsException(e.getMessage());
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String getJwtTokenFromRequestCookie(HttpServletRequest httpServletRequest){
        return Arrays.stream(httpServletRequest.getCookies())
                .filter(c -> c.getName().equals(jwtConfig.getAuthorizationHeader()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }
}
