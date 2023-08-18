package com.tsa.movieland.dao;

import com.tsa.movieland.dto.PosterDto;

public interface PosterDao {
    void add(int movieId, String posterLink);
    Iterable<PosterDto> findPosterByMovieId(int movieId);
    void update(int movieId, String picturePath);
}
