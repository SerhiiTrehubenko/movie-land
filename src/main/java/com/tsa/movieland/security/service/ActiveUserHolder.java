package com.tsa.movieland.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class ActiveUserHolder {

    private final JwtService jwtService;

    private final Map<String, Date> usersTimeStamps = new ConcurrentHashMap<>();

    public void add(String email) {
        usersTimeStamps.put(email, new Date(System.currentTimeMillis() - 1000));
    }

    public void remove(String userEmailExpiredToken) {
        usersTimeStamps.remove(userEmailExpiredToken);
    }

    public String getUserEmail(String authHeader) {
        String jwtToken = jwtService.getToken(authHeader);
        String userEmail = getEmail(jwtToken);
        Date issueAt = jwtService.getIssueAt(jwtToken);

        if (usersTimeStamps.containsKey(userEmail)) {
            if (issueAt.after(usersTimeStamps.get(userEmail))) {
                return userEmail;
            }
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
        usersTimeStamps.remove(userEmailExpiredToken);

        return userEmailExpiredToken;
    }
}
