package com.tsa.movieland.repository;

import com.tsa.movieland.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@com.tsa.movieland.context.JpaRepository
public interface ReviewRepository extends JpaRepository<ReviewEntity, ReviewEntity.PrimaryKey> {

    List<ReviewEntity> findByMovieIdOrderByRecordTime(int movieId);
}
