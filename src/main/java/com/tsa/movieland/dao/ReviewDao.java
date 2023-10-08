package com.tsa.movieland.dao;

import com.tsa.movieland.entity.Review;

import java.util.List;

public interface ReviewDao {

    List<Review> findByMovieId(int movieId);

    void save(Review review);
}
