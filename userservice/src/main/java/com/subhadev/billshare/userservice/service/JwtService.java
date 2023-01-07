package com.subhadev.billshare.userservice.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.AlgorithmConstraints;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    @Value("${app.jwt.secret}")
    private  String SECRET = "xccjcndkjcnsdkvnsdvnsdvnjksnvjksnvksdnkvj";
    @Value("${app.jwt.expiration}")
    private Long EXPIRATION = 3600000l;
    Algorithm algorithm = Algorithm.HMAC256(SECRET);

    public String createJwt(String userId, List<String> userRoles) {
        if (!StringUtils.hasText(userId) ) {
            throw new IllegalArgumentException("Can't create JWT with blank string");
        }

        return JWT.create().withSubject(userId)
                .withIssuedAt(new Date())
                .withIssuer(userId)
                .withClaim("roles",userRoles)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION))
                .sign(algorithm);
    }

    public String findUsernameFromJWT(String jwtString) {
        return JWT.require(algorithm)
                .build()
                .verify(jwtString)
                .getSubject();
    }
}
