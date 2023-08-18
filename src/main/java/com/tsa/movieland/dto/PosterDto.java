package com.tsa.movieland.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class PosterDto {
    private final int movieId;
    private final String link;
}
