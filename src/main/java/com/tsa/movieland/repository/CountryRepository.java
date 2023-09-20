package com.tsa.movieland.repository;

import com.tsa.movieland.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@com.tsa.movieland.context.JpaRepository
public interface CountryRepository extends JpaRepository<Country, Integer> {
    @Query(value = "SELECT country_id, country_name FROM countries_by_movie_id WHERE movie_id = :movieId", nativeQuery = true)
    List<Country> findAllByMovieId(int movieId);
}
