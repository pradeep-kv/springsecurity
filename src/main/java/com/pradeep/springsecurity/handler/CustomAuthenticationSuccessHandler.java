package com.pradeep.springsecurity.handler;

import com.pradeep.springsecurity.config.JwtConfig;
import com.pradeep.springsecurity.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    JwtUtil jwtUtil;

    final JwtConfig jwtConfig;

    @Autowired
    public CustomAuthenticationSuccessHandler(JwtUtil jwtUtil, JwtConfig jwtConfig) {
        this.jwtUtil = jwtUtil;
        this.jwtConfig = jwtConfig;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        String token = jwtUtil.generateToken(authentication);
        httpServletResponse.setHeader(jwtConfig.getAuthorizationHeader(), jwtConfig.getTokenPrefix() + " " + token);
        httpServletResponse.getWriter().write("Auth Success");
        httpServletResponse.getWriter().flush();
        httpServletResponse.getWriter().close();
    }

}
