package com.tsa.movieland.security.controller;

import com.tsa.movieland.dto.UserRegistration;
import com.tsa.movieland.entity.User;
import com.tsa.movieland.security.common.AuthenticationRequest;
import com.tsa.movieland.security.common.AuthenticationResponse;
import com.tsa.movieland.security.service.ActiveUserHolder;
import com.tsa.movieland.security.service.JwtService;
import com.tsa.movieland.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final ActiveUserHolder activeUserHolder;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUserEmail(), request.getPassword()
                )
        );

        User user = userService.getUserByEmail(request.getUserEmail());

        UserRegistration userRegistration = UserRegistration.builder()
                .email(user.getEmail())
                .build();

        activeUserHolder.add(userRegistration.getEmail());
        log.info("Successfully login");
        return ResponseEntity.ok().body(getAuthenticationResponse(userRegistration));
    }

    private AuthenticationResponse getAuthenticationResponse(UserRegistration user) {
        String token = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .response(token)
                .build();
    }
}
