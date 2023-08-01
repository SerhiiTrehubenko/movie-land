package com.tsa.movieland.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Genre {
    private final int id;
    private final String name;
}
