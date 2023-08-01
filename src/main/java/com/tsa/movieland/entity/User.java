package com.tsa.movieland.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class User {
    private int id;
    private String nickname;
}
