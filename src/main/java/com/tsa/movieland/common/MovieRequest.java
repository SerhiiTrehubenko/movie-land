package com.tsa.movieland.common;

import lombok.*;

@Builder
@Getter
@ToString
public class MovieRequest {

    public final static MovieRequest EMPTY_MOVIE_REQUEST = new MovieRequest(null, null);

    private SortField sortField;
    private SortDirection sortDirection;
}

