package com.tsa.movieland.dao.jpa.repository;

import com.tsa.movieland.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {

    @Query(value = "SELECT c FROM MovieCountry mc JOIN Country c ON mc.countryId = c.id WHERE mc.movieId = :movieId")
    List<Country> findAllByMovieId(int movieId);
}
