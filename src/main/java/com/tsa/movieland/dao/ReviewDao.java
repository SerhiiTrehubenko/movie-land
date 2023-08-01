package com.tsa.movieland.dao;

import com.tsa.movieland.entity.Review;

public interface ReviewDao {

    Iterable<Review> findByMovieId(int movieId);
}
