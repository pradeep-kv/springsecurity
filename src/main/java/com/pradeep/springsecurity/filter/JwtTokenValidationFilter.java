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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        String requestHeader = httpServletRequest.getHeader(jwtConfig.getAuthorizationHeader());
        if(Strings.isNotBlank(requestHeader) && Strings.isNotEmpty(requestHeader)
                && requestHeader.startsWith(jwtConfig.getTokenPrefix())){
            String token = requestHeader.replace(jwtConfig.getTokenPrefix(), "");

            try{
                Authentication authentication = jwtUtil.verifyToken(token);
                if(authentication != null && SecurityContextHolder.getContext().getAuthentication() == null){
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }else {
                    throw new BadCredentialsException("Invalid Token");
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
