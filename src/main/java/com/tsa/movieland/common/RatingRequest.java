package com.tsa.movieland.common;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class RatingRequest {
    private String userEmail;
    private int movieId;
    double rating;
}
