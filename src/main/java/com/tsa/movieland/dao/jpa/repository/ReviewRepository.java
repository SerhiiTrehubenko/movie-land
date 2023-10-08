package com.tsa.movieland.dao.jpa.repository;

import com.tsa.movieland.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends
        JpaRepository<Review, Review.ReviewId> {

    @Query(value = "SELECT r FROM Review r LEFT JOIN FETCH r.user WHERE r.reviewId.movieId = :movieId")
    List<Review> findAllByMovieIdOrderByRecordTime(int movieId);
}
