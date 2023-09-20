package com.tsa.movieland.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Builder
@Getter
@Setter
public class PosterUpdate {
    private int movieId;
    private String link;
    private Timestamp recordTime;
}
