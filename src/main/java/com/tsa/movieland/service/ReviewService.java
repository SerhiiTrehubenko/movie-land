package com.tsa.movieland.service;

import com.tsa.movieland.dto.AddReviewRequest;
import com.tsa.movieland.dto.ReviewDto;

public interface ReviewService {
    Iterable<ReviewDto> findByMovieId(int movieId);

    void save(int userId, AddReviewRequest reviewRequest);
}
