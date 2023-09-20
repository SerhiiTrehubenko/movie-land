package com.tsa.movieland.dao;

import com.tsa.movieland.dto.AddReviewRequest;
import com.tsa.movieland.dto.ReviewDto;

public interface ReviewDao {

    Iterable<ReviewDto> findByMovieId(int movieId);

    void save(int userId, AddReviewRequest reviewRequest);
}
