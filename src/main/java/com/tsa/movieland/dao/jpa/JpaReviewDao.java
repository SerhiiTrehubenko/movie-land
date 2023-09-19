package com.tsa.movieland.dao.jpa;

import com.tsa.movieland.context.JpaDao;
import com.tsa.movieland.dao.ReviewDao;
import com.tsa.movieland.dto.AddReviewRequest;
import com.tsa.movieland.entity.Review;
import com.tsa.movieland.entity.ReviewEntity;
import com.tsa.movieland.mapper.ReviewMapper;
import com.tsa.movieland.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@JpaDao
@RequiredArgsConstructor
public class JpaReviewDao implements ReviewDao {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    @Override
    public Iterable<Review> findByMovieId(int movieId) {
        final List<ReviewEntity> byMovieId = reviewRepository.findByMovieIdOrderByRecordTime(movieId);
        return byMovieId.stream().map(reviewMapper::toReviewDto).toList();
    }

    @Override
    public void save(int userId, AddReviewRequest reviewRequest) {
        final ReviewEntity reviewEntity = ReviewEntity.builder()
                .movieId(reviewRequest.getMovieId())
                .userId(userId)
                .text(reviewRequest.getText())
                .recordTime(new Timestamp(System.currentTimeMillis()))
                .build();
        reviewRepository.save(reviewEntity);
    }
}
