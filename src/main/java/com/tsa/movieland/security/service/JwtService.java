package com.tsa.movieland.security.service;

import com.tsa.movieland.dto.UserRegistration;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Service
public class JwtService {

    private final static int AFTER_BEARER = 7;
    @Value("${token.expiration.period}")
    private Integer expirationTime;
    @Value("${token.time.unit}")
    String timeType;

    private final static String SECRET_KEY = "7e53331fda74905f903342f474eb99a8754b26aea4a614a6bf8c40e7ab36aed5";

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public <T> T extractClaim(Function<Claims, T> claimsResolver, Claims claims) {
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token).getBody();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserRegistration userDetails, Map<String, Object> claims) {
        return createToken(userDetails, claims);
    }

    private String createToken(UserRegistration userDetails, Map<String, Object> claims) {
        final TimeUnit timeUnit = TimeUnit.valueOf(timeType);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + timeUnit.toMillis(expirationTime)))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(UserRegistration userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return generateToken(userDetails, claims);
    }

    public String getToken(String header) {
        return header.substring(AFTER_BEARER);
    }

    public Date getIssueAt(String token) {
        return extractClaim(token, Claims::getIssuedAt);
    }
}
