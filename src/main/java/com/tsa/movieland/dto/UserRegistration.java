package com.tsa.movieland.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserRegistration {
    private String firstName;
    private String lastName;
    private String nickname;
    private String email;
    private String password;
}
