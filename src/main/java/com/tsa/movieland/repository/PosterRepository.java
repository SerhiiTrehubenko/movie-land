package com.tsa.movieland.repository;

import com.tsa.movieland.entity.PosterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@com.tsa.movieland.context.JpaRepository
public interface PosterRepository extends JpaRepository<PosterEntity, PosterEntity.PrimaryKey> {
    List<PosterEntity> findAllByMovieIdOrderByRecordTime(int movieId);
}
