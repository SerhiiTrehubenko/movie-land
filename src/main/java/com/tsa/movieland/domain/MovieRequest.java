package com.tsa.movieland.domain;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
public class MovieRequest {
    private SortField sortField;
    private SortDirection sortDirection;

    public static class EmptyMovieRequest extends MovieRequest {
        public EmptyMovieRequest() {
            super(null, null);
        }
    }
}

