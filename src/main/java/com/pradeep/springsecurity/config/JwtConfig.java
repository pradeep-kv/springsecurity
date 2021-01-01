package com.pradeep.springsecurity.config;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtConfig {
    @Value("${jwt.access-token-validity-seconds}")
    private Integer accessTokenValidity;

    @Value("${jwt.privatekey}")
    private String privateKey;

    @Value("${jwt.publickey}")
    private String publicKey;

//    @Value("${jwt.signatureAlgorithm}")
//    private static String signatureAlgorithm;

    @Value("${jwt.tokenprefix}")
    private String tokenPrefix;

    @Value("${jwt.authorizationheader}")
    private String authorizationHeader;

    public int getAccessTokenValidity() {
        return accessTokenValidity;
    }

//    public void setAccessTokenValidity(int accessTokenValidity) {
//        this.accessTokenValidity = accessTokenValidity;
//    }

    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(privateKey.getBytes());
    }

//    public void setPrivateKey(String privateKey) {
//        this.privateKey = privateKey;
//    }

    //    public void setPublicKey(String publicKey) {
//        this.publicKey = publicKey;
//    }

//    public static SignatureAlgorithm getSignatureAlgorithm() {
//        SignatureAlgorithm algo = SignatureAlgorithm.valueOf(signatureAlgorithm);
//        return algo;
//    }

//    public static void setSignatureAlgorithm(String signatureAlgorithm) {
//        JwtConfig.signatureAlgorithm = signatureAlgorithm;
//    }


    public String getTokenPrefix() {
        return tokenPrefix;
    }

//    public void setTokenPrefix(String tokenPrefix) {
//        this.tokenPrefix = tokenPrefix;
//    }

    public String getAuthorizationHeader() {
        return authorizationHeader;
    }

//    public void setAuthorizationHeader(String authorizationHeader) {
//        this.authorizationHeader = authorizationHeader;
//    }
}
