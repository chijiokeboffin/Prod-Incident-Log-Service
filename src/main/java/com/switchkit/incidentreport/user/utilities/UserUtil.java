package com.switchkit.incidentreport.user.utilities;

import com.auth0.jwt.algorithms.Algorithm;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;


@Component
@Slf4j
public  class UserUtil {
    private final  String BEARER = "Bearer ";


    @Value("${jwt.token.secret}")
    private String path;

    public String getToken(){
        log.info("Testing " + this.path);
        return new UserUtil().path ;
    }



    public static Algorithm getAlgorithm(){
        Algorithm algorithm = Algorithm.HMAC256("UEBzc3dvcmQxMCQ=".getBytes());
        return algorithm;
    }
    @Value("${jwt.token.secret}")
    private String secret;

    public Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.secret);
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
