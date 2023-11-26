package com.tsa.movieland.service;

import com.tsa.movieland.dao.ReviewDao;
import com.tsa.movieland.dto.AddReviewRequest;
import com.tsa.movieland.dto.ReviewDto;
import com.tsa.movieland.entity.Review;
import com.tsa.movieland.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class DefaultReviewService implements ReviewService {

    private final ReviewDao reviewDao;
    private final ReviewMapper reviewMapper;

    @Override
    @Transactional(readOnly = true)
    public Iterable<ReviewDto> findByMovieId(int movieId) {
        return reviewDao.findByMovieId(movieId)
                .stream()
                .sorted(Comparator.comparing(Review::getRecordTime))
                .map(reviewMapper::toReviewDto)
                .toList();
    }

    @Override
    @Transactional
    public void save(int userId, AddReviewRequest reviewRequest) {
        Review review = createReview(userId, reviewRequest);

        reviewDao.save(review);
    }

    private Review createReview(int userId, AddReviewRequest reviewRequest) {
        return Review.builder()
                .reviewId(Review.ReviewId.builder().movieId(reviewRequest.getMovieId()).userId(userId).build())
                .text(reviewRequest.getText())
                .recordTime(new Timestamp(System.currentTimeMillis()))
                .build();
    }
}
