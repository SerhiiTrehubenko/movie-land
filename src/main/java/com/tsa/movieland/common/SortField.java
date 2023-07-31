package com.tsa.movieland.common;

public enum SortField {
    RATING("movie_rating"),
    PRICE("movie_price");

    private final String columnName;

    SortField(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }
}
