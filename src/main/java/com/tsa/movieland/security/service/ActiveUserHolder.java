package com.tsa.movieland.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ActiveUserHolder {

    private final JwtService jwtService;

    private final Set<String> emails = Collections.synchronizedSet(new HashSet<>());

    public void add(String email) {
        emails.add(email);
    }

    public void remove(String userEmailExpiredToken) {
        emails.remove(userEmailExpiredToken);
    }

    public String getUserEmail(String authHeader) {
        String jwtToken = jwtService.getToken(authHeader);
        String userEmail = getEmail(jwtToken);

        if (emails.contains(userEmail)) {
            return userEmail;
        }
        return null;
    }

    private String getEmail(String jwtToken) {
        Claims claims;

        try {
            return jwtService.extractUserName(jwtToken);
        } catch (ExpiredJwtException e) {
            claims = e.getClaims();
        }

        final String userEmailExpiredToken = jwtService.extractClaim(Claims::getSubject, claims);
        emails.remove(userEmailExpiredToken);

        return userEmailExpiredToken;
    }
}
