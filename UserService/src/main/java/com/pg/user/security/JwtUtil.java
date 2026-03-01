package com.pg.user.security;

import io.jsonwebtoken.Claims;
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


    public String generateToken(String username,int expiryMinute,String role,String userId){
        return Jwts.builder().setSubject(username).claim("role",role).claim("userId",userId).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis()+expiryMinute*60*1000)).signWith(key, SignatureAlgorithm.HS256).compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean validateToken(String token, String username) {
        String extractedUsername = extractUsername(token);
        return extractedUsername.equals(username) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
