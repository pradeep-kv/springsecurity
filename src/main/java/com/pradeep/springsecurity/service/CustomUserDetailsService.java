package com.pradeep.springsecurity.service;

import com.pradeep.springsecurity.config.CustomPasswordEncoder;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Profile("dev")
public class CustomUserDetailsService implements UserDetailsService{
    private List<UserDetails> applicationUsers = new ArrayList<>();

    public CustomUserDetailsService(){
               applicationUsers.addAll(Arrays.asList(
                new User(
                        "user",
                        CustomPasswordEncoder.getInstance().getDelegatingPasswordEncoder().encode("password"),
                        true,
                        true,
                        true,
                        true,
                        AuthorityUtils.NO_AUTHORITIES
                ),
                new User(
                        "linda",
                        CustomPasswordEncoder.getInstance().getDelegatingPasswordEncoder().encode("password"),
                        true,
                        true,
                        true,
                        true,
                        AuthorityUtils.NO_AUTHORITIES
                ),
                new User(
                        "tom",
                        CustomPasswordEncoder.getInstance().getDelegatingPasswordEncoder().encode("password"),
                        true,
                        true,
                        true,
                        true,
                        AuthorityUtils.NO_AUTHORITIES
                )
        ));
    }

//    public static UserDetailsService getUserDetailsService(){
//        UserDetails users = User.builder()
//                .username("user")
//                .password(CustomPasswordEncoder.getPasswordEncoder().encode("password"))
//                .authorities("ADMIN")
//                .username("user")
//                .password(CustomPasswordEncoder.getPasswordEncoder().encode("password"))
//                .authorities("ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(users);
//    }


    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may possibly be case sensitive, or case insensitive depending on how thereturn null;
     * implementation instance is configured. In this case, the <code>UserDetails</code>
     * object that comes back may have a username that is of a different case than what
     * was actually requested..
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     *                                   GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return applicationUsers
                .stream()
                .filter(applicationUser -> username.equals(applicationUser.getUsername()))
                .findFirst().orElseThrow(() ->
                        new UsernameNotFoundException(String.format("Username %s not found", username))
                );
    }
}
