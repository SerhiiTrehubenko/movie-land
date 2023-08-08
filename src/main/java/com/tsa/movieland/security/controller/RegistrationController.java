package com.tsa.movieland.security.controller;

import com.tsa.movieland.dto.UserRegistration;
import com.tsa.movieland.security.common.AuthenticationResponse;
import com.tsa.movieland.security.service.ActiveUserHolder;
import com.tsa.movieland.security.service.JwtService;
import com.tsa.movieland.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/registration", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class RegistrationController {

    private final ActiveUserHolder activeUserHolder;
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<AuthenticationResponse> register(@RequestBody UserRegistration request) {
        userService.save(request);
        activeUserHolder.add(request.getEmail());

        return ResponseEntity.ok(getAuthenticationResponse(request));
    }

    private AuthenticationResponse getAuthenticationResponse(UserRegistration user) {
        String token = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .response(token)
                .build();
    }
}
