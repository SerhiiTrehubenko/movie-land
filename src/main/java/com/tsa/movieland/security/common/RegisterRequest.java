package com.tsa.movieland.security.common;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String nickname;
    private String email;
    private String password;
}
