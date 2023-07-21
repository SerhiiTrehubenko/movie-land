package com.tsa.movieland.util;

import lombok.*;

@Builder
@Getter
@ToString
public class DefaultMovieRequest implements MovieRequest {
    private SortField sortField;
    private SortDirection sortDirection;
}

