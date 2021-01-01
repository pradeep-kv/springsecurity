package com.pradeep.springsecurity.config;

import com.pradeep.springsecurity.filter.EmailConfigFilter;
import com.pradeep.springsecurity.filter.JwtTokenValidationFilter;
import com.pradeep.springsecurity.handler.CustomAuthenticationSuccessHandler;
import com.pradeep.springsecurity.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
//    @Autowired
//    EmailConfigFilter emailConfigFilter;

    CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    final JwtTokenValidationFilter jwtTokenValidationFilter;

    @Autowired
    public SecurityConfiguration(CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler, JwtTokenValidationFilter jwtTokenValidationFilter) {
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
        this.jwtTokenValidationFilter = jwtTokenValidationFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()   //Disable csrf token...
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//                .loginProcessingUrl("/login")
//                .successHandler(new CustomAuthenticationSuccessHandler())
//                .permitAll();

        http.addFilterBefore(new EmailConfigFilter("/authenticate-email",
                        authenticationManager(), customAuthenticationSuccessHandler),
                UsernamePasswordAuthenticationFilter.class);

        http.addFilterAfter(jwtTokenValidationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authProvider());
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(CustomUserDetailsService.getUserDetailsService());
        authProvider.setPasswordEncoder(CustomPasswordEncoder.getPasswordEncoder());
        return authProvider;
    }
}
