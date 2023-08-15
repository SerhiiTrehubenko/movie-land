package com.tsa.movieland.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Poster {
    private final int movieId;
    private final String link;
}
