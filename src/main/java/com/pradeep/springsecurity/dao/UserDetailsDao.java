package com.pradeep.springsecurity.dao;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsDao {
    UserDetails loadUserByUsername(String username);
}
