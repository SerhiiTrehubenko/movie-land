package com.tsa.movieland.repository;

import com.tsa.movieland.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@com.tsa.movieland.context.JpaRepository
public interface ReviewRepository extends JpaRepository<Review, Review.PrimaryKey> {

    List<Review> findByMovieIdOrderByRecordTime(int movieId);
}
