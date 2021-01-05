package com.pradeep.springsecurity.handler;

import com.pradeep.springsecurity.config.JwtConfig;
import com.pradeep.springsecurity.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
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
//        httpServletResponse.setHeader(jwtConfig.getAuthorizationHeader(), jwtConfig.getTokenPrefix() + " " + token);
//        After careful consideration, It was decided to keep JWT Token in Cookie
//        Below is the reference article about keeping JWT Token in Header vs Cookie
//        https://stormpath.com/blog/where-to-store-your-jwts-cookies-vs-html5-web-storage
        httpServletResponse.addCookie(new Cookie(jwtConfig.getAuthorizationHeader(), jwtConfig.getTokenPrefix() + token));
//        final Cookie cookie = new Cookie(this.cookieName, principal.getSignedJWT());
//        cookie.setDomain(this.cookieDomain);
//        cookie.setSecure(this.sendSecureCookie);
//        cookie.setHttpOnly(true);
//        cookie.setMaxAge(maxAge);
//        response.addCookie(cookie);
        httpServletResponse.getWriter().write("Auth Success");
        httpServletResponse.getWriter().flush();
        httpServletResponse.getWriter().close();
    }

}
