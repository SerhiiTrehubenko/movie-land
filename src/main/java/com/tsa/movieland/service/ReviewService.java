package com.tsa.movieland.service;

import com.tsa.movieland.entity.Review;

public interface ReviewService {
    Iterable<Review> findByMovieId(int movieId);
}
