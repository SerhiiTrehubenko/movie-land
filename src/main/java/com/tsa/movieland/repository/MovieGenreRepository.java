package com.tsa.movieland.repository;

import com.tsa.movieland.entity.MovieGenre;
import org.springframework.data.jpa.repository.JpaRepository;

@com.tsa.movieland.context.JpaRepository
public interface MovieGenreRepository extends JpaRepository<MovieGenre, MovieGenre.PrimaryKey> {
    void deleteAllByMovieId(int movieId);
}
