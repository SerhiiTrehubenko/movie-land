package com.tsa.movieland.dao.jpa.repository;

import com.tsa.movieland.entity.MovieGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieGenreRepository extends JpaRepository<MovieGenre, MovieGenre.PrimaryKey> {
    void deleteAllByMovieId(int movieId);
}
