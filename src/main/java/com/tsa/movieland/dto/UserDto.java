package com.tsa.movieland.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class UserDto {
    private int id;
    private String nickname;
}
