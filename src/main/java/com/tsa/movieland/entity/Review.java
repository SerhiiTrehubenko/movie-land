package com.tsa.movieland.entity;

import com.tsa.movieland.dto.UserDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Review {
    private UserDto user;
    private String text;
}
