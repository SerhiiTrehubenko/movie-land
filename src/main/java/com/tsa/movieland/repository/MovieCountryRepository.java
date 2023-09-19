package com.tsa.movieland.repository;

import com.tsa.movieland.entity.MovieCountry;
import org.springframework.data.jpa.repository.JpaRepository;

@com.tsa.movieland.context.JpaRepository
public interface MovieCountryRepository extends JpaRepository<MovieCountry, MovieCountry.PrimaryKey> {
    void deleteAllByMovieId(int movieId);
}
