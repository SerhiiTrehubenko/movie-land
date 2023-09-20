package com.tsa.movieland.repository;

import com.tsa.movieland.entity.Poster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@com.tsa.movieland.context.JpaRepository
public interface PosterRepository extends JpaRepository<Poster, Poster.PrimaryKey> {
    List<Poster> findAllByMovieIdOrderByRecordTime(int movieId);
}
