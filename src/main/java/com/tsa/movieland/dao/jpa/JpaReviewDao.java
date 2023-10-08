package com.tsa.movieland.dao.jpa;

import com.tsa.movieland.context.JpaDao;
import com.tsa.movieland.dao.ReviewDao;
import com.tsa.movieland.entity.Review;
import com.tsa.movieland.dao.jpa.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@JpaDao
@RequiredArgsConstructor
public class JpaReviewDao implements ReviewDao {

    private final ReviewRepository reviewRepository;

    @Override
    public List<Review> findByMovieId(int movieId) {
        return reviewRepository.findAllByMovieIdOrderByRecordTime(movieId);
    }

    @Override
    public void save(Review review) {
        reviewRepository.save(review);
    }
}
