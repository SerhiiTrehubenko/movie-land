package com.tsa.movieland.repository;

import com.tsa.movieland.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@com.tsa.movieland.context.JpaRepository
public interface GenreRepository extends JpaRepository<Genre, Integer> {
    @Query(value = "SELECT genre_id, genre_name FROM genres_by_movie_id WHERE movie_id = :movieId", nativeQuery = true)
    List<Genre> findByMovieId(int movieId);
}
