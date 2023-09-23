package com.tsa.movieland.repository;

import com.tsa.movieland.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@com.tsa.movieland.context.JpaRepository
public interface CountryRepository extends JpaRepository<Country, Integer> {

    @Query(value = "SELECT c FROM MovieCountry mc JOIN Country c ON mc.countryId = c.id WHERE mc.movieId = :movieId")
    List<Country> findAllByMovieId(int movieId);
}
