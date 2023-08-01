package com.tsa.movieland.service;

import com.tsa.movieland.dao.ReviewDao;
import com.tsa.movieland.entity.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultReviewService implements ReviewService {

    private final ReviewDao reviewDao;

    @Override
    public Iterable<Review> findBiMovieId(int movieId) {
        return reviewDao.findByMovieId(movieId);
    }
}
