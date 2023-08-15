package com.tsa.movieland.dao;

import com.tsa.movieland.entity.Poster;

public interface PosterDao {
    void addPoster(int movieId, String posterLink);
    Iterable<Poster> findPosterByMovieId(int movieId);
}
