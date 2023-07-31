package com.tsa.movieland.entity;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Genre {
    private final int id;
    private final String name;
}
