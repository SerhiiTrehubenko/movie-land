package com.tsa.movieland.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Review {
    private User user;
    private String text;
}
