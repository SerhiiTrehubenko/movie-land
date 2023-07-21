package com.tsa.movieland.util;

public class EmptyMovieRequest implements MovieRequest {
    @Override
    public SortField getSortField() {
        return null;
    }

    @Override
    public SortDirection getSortDirection() {
        return null;
    }
}
