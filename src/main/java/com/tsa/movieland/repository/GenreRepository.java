package com.tsa.movieland.repository;

import com.tsa.movieland.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@com.tsa.movieland.context.JpaRepository
public interface GenreRepository extends JpaRepository<Genre, Integer> {
    @Query(value = "SELECT g FROM MovieGenre mg JOIN Genre g ON mg.genreId = g.id WHERE mg.movieId = :movieId")
    List<Genre> findByMovieId(int movieId);
}
