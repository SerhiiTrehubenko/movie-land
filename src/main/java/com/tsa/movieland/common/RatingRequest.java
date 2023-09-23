package com.tsa.movieland.common;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Builder
@Getter
@ToString
public class RatingRequest {
    private String userEmail;
    private int movieId;
    double rating;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RatingRequest that = (RatingRequest) o;
        return movieId == that.movieId &&
                Objects.equals(userEmail, that.userEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userEmail, movieId);
    }
}
