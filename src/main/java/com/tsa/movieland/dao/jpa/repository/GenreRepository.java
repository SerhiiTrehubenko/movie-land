package com.tsa.movieland.dao.jpa.repository;

import com.tsa.movieland.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Integer> {
    @Query(value = "SELECT g FROM MovieGenre mg JOIN Genre g ON mg.genreId = g.id WHERE mg.movieId = :movieId")
    List<Genre> findByMovieId(int movieId);
}
