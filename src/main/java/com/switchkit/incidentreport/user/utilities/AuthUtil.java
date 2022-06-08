package com.switchkit.incidentreport.user.utilities;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.switchkit.incidentreport.exception.AuthenticationFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@Slf4j
public class AuthUtil {
    private final static long EXPIRATION_TIME = 10 * 60 * 1000;

    @Value("${jwt.token.secret}")
    private String secret;

    public static void sendAccessToken(HttpServletRequest request, HttpServletResponse response,
                                       FilterChain chain, User user, List<String> roles) throws IOException {

        Algorithm algorithm = UserUtil.getAlgorithm();
        String access_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .withIssuer(request.getRequestURI())
                .withClaim("roles", roles)
                .sign(algorithm);

        String refresh_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() +  EXPIRATION_TIME))
                .withIssuer(request.getRequestURI())
                .withClaim("roles", roles)
                .sign(algorithm);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        tokens.put("refresh_token", refresh_token);
        tokens.put("expire_at", String.valueOf(EXPIRATION_TIME));

        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }
    public static String getUserName(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getPrincipal().toString();
        if(Objects.isNull(username)){
            throw new AuthenticationFailedException("Unknown user");
        }
        return username;
    }
}
