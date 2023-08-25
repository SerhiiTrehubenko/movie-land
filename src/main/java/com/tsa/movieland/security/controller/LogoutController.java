package com.tsa.movieland.security.controller;

import com.tsa.movieland.security.common.AuthenticationResponse;
import com.tsa.movieland.security.service.ActiveUserHolder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class LogoutController {
    private final ActiveUserHolder activeUserHolder;

    @DeleteMapping
    public ResponseEntity<AuthenticationResponse> logout(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION);

        if (Objects.isNull(authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        String userEmail = activeUserHolder.getUserEmail(authHeader);

        if (Objects.nonNull(userEmail)) {
            log.info("Successfully logout");
            activeUserHolder.remove(userEmail);
            return ResponseEntity.ok().body(createResponse());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    private AuthenticationResponse createResponse() {
        return AuthenticationResponse.builder()
                .response("You have been successfully logout")
                .build();
    }
}
