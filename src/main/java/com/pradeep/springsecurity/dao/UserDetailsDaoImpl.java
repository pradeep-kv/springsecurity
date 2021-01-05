package com.pradeep.springsecurity.dao;

import com.pradeep.springsecurity.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Transactional
@Repository
public class UserDetailsDaoImpl implements UserDetailsDao{

    @Autowired
    JdbcTemplate jdbcTemplateCatalystDb;

    public static final String DEF_USERS_BY_USERNAME_QUERY = "select id_user,user_name, user_email, user_mobile," +
            "password,user_disabled, user_expired, user_locked, user_credentials_expired "
            + "from users "
            + "where user_name = ?";

    @Override
    public UserDetails loadUserByUsername(String username) {
        List<UserDetails> users = loadUsersByUsername(username);
        if (users.size() == 0) {
//            this.logger.debug("Query returned no results for user '" + username + "'");
            throw new UsernameNotFoundException( "Username {0} not found");
        }
        UserDetails user = users.get(0); // contains no GrantedAuthority[]
        Set<GrantedAuthority> dbAuthsSet = new HashSet<>();
//        dbAuthsSet.addAll(loadUserAuthorities(user.getUsername()));

        List<GrantedAuthority> dbAuths = new ArrayList<>(dbAuthsSet);
//        addCustomAuthorities(user.getUsername(), dbAuths);
        if (dbAuths.size() == 0) {
//            this.logger.debug("User '" + username + "' has no authorities and will be treated as 'not found'");
//            throw new UsernameNotFoundException(this.messages.getMessage("JdbcDaoImpl.noAuthority",
//                    new Object[] { username }, "User {0} has no GrantedAuthority"));
        }
        return createUserDetails(username, user, dbAuths);
    }

    protected UserDetails createUserDetails(String username, UserDetails userFromUserQuery,
                                            List<GrantedAuthority> combinedAuthorities) {
        String returnUsername = userFromUserQuery.getUsername();
        com.pradeep.springsecurity.model.User user = (User) userFromUserQuery;
//        if (!this.usernameBasedPrimaryKey) {
//            returnUsername = username;
//        }
//        return new User(returnUsername, userFromUserQuery.getPassword(), userFromUserQuery.isEnabled(),
//                userFromUserQuery.isAccountNonExpired(), userFromUserQuery.isCredentialsNonExpired(),
//                userFromUserQuery.isAccountNonLocked(), combinedAuthorities);
        return new com.pradeep.springsecurity.model.User(user.getIduser(), user.getUsername(),
                user.getUseremail(), user.getUsermobile(), user.getPassword(),user.isEnabled(),
                user.isAccountNonExpired(), user.isCredentialsNonExpired(), user.isAccountNonLocked(),
                combinedAuthorities);
    }

    protected List<UserDetails> loadUsersByUsername(String username) {
        // @formatter:off
        RowMapper<UserDetails> mapper = (rs, rowNum) -> {
            Integer idUser = rs.getInt(1);
            String username1 = rs.getString(2);
            String userEmail = rs.getString(3);
            String userMobile = rs.getString(4);
            String password = rs.getString(5);
            boolean enabled = rs.getBoolean(6);
            boolean expired = rs.getBoolean(7);
            boolean locked = rs.getBoolean(8);
            boolean credentialsNonExpired = rs.getBoolean(9);

//            return new User(username1, password, enabled, true, true, true, AuthorityUtils.NO_AUTHORITIES);
            return new com.pradeep.springsecurity.model.User(idUser, username1, userEmail, userMobile, password, enabled,
                                                            expired, credentialsNonExpired, locked, AuthorityUtils.NO_AUTHORITIES);
        };
        // @formatter:on
        return jdbcTemplateCatalystDb.query(DEF_USERS_BY_USERNAME_QUERY, mapper, username);
    }
}
