package com.pg.user.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String jwtSecretKey="your-secure-secret-key-min-32bytes";
    private static final Key key= Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));


    public String generateToken(String username,int expiryMinute){
        return Jwts.builder().setSubject(username).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis()+expiryMinute*60*100)).signWith(key, SignatureAlgorithm.HS256).compact();
    }
}
