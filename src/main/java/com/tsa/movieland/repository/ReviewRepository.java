package com.tsa.movieland.repository;

import com.tsa.movieland.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@com.tsa.movieland.context.JpaRepository
public interface ReviewRepository extends
        JpaRepository<Review, Review.PrimaryKey> {

    @Query(value = "SELECT r FROM Review r LEFT JOIN FETCH r.user WHERE r.movieId = :movieId")
    List<Review> findByMovieIdOrderByRecordTime(int movieId);
}
