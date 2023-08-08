package com.tsa.movieland.entity;

import com.tsa.movieland.common.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Builder
@Setter
@ToString
public class Credentials {
    private int userId;
    private String password;
    private Role role;
}
