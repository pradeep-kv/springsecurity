package com.pradeep.springsecurity.service;

import com.pradeep.springsecurity.config.CustomPasswordEncoder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

public class CustomUserDetailsService {

    public static UserDetailsService getUserDetailsService(){
        UserDetails users = User.builder()
                .username("user")
                .password(CustomPasswordEncoder.getPasswordEncoder().encode("password"))
                .authorities("ADMIN")
                .username("user")
                .password(CustomPasswordEncoder.getPasswordEncoder().encode("password"))
                .authorities("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(users);
    }



}
