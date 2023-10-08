package com.tsa.movieland.dao.jpa.repository;

import com.tsa.movieland.entity.MovieCountry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieCountryRepository extends JpaRepository<MovieCountry, MovieCountry.PrimaryKey> {
    void deleteAllByMovieId(int movieId);
}
