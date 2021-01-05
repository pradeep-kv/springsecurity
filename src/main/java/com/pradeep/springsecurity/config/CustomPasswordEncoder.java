package com.pradeep.springsecurity.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.*;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CustomPasswordEncoder {
    private static final String idForEncode = "bcrypt";
    private static Map<String, PasswordEncoder> encoders = new HashMap<>();
    private static CustomPasswordEncoder customPasswordEncoder = null;
    private  DelegatingPasswordEncoder delegatingPasswordEncoder;
    private CustomPasswordEncoder(){
        encoders.put(idForEncode, new BCryptPasswordEncoder());
        encoders.put("noop", NoOpPasswordEncoder.getInstance());
        encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
        encoders.put("scrypt", new SCryptPasswordEncoder());
        encoders.put("sha256", new StandardPasswordEncoder());
        delegatingPasswordEncoder = new DelegatingPasswordEncoder(idForEncode,encoders);
    }

    public static CustomPasswordEncoder getInstance(){
       if(customPasswordEncoder == null){
           customPasswordEncoder = new CustomPasswordEncoder();
       }
       return customPasswordEncoder;
    }

    public DelegatingPasswordEncoder getDelegatingPasswordEncoder(){
        return delegatingPasswordEncoder;
    }

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }
}
