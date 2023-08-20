package com.tsa.movieland.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AddReviewRequest {
    private Integer movieId;
    private String text;
}
