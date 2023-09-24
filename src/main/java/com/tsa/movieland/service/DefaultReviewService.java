package com.tsa.movieland.service;

import com.tsa.movieland.dao.ReviewDao;
import com.tsa.movieland.dto.AddReviewRequest;
import com.tsa.movieland.dto.ReviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DefaultReviewService implements ReviewService {

    private final ReviewDao reviewDao;

    @Override
    @Transactional(readOnly = true)
    public Iterable<ReviewDto> findByMovieId(int movieId) {
        return reviewDao.findByMovieId(movieId);
    }

    @Override
    @Transactional
    public void save(int user, AddReviewRequest reviewRequest) {
        reviewDao.save(user, reviewRequest);
    }
}
