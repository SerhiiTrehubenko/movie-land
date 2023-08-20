package com.tsa.movieland.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActiveUserHolder {
    private final static long LAG_FOR_REGISTRATION_DATE = 1000L;
    private final JwtService jwtService;

    private final Map<String, Date> usersTimeStamps = new ConcurrentHashMap<>();

    public void add(String email) {
        usersTimeStamps.put(email, new Date(System.currentTimeMillis() - LAG_FOR_REGISTRATION_DATE));
    }

    public void remove(String userEmail) {
        usersTimeStamps.remove(userEmail);
    }

    public String getUserEmail(String authHeader) {
        String jwtToken = jwtService.getToken(authHeader);
        String userEmail = getEmail(jwtToken);

        if (usersTimeStamps.containsKey(userEmail)) {
            Date issueAt = jwtService.getIssueAt(jwtToken);
            if (issueAt.after(usersTimeStamps.get(userEmail))) {
                return userEmail;
            }
        }
        usersTimeStamps.remove(userEmail);
        log.info("User [{}] tried to get access with token that was logout", userEmail);
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
        log.info("User [{}] tried to use expired token, as result was removed from active users", userEmailExpiredToken);
        usersTimeStamps.remove(userEmailExpiredToken);

        return userEmailExpiredToken;
    }
}
