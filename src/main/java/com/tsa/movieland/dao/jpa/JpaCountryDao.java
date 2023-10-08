package com.tsa.movieland.dao.jpa;

import com.tsa.movieland.context.JpaDao;
import com.tsa.movieland.dao.CountryDao;
import com.tsa.movieland.entity.Country;
import com.tsa.movieland.dao.jpa.repository.CountryRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@JpaDao
@RequiredArgsConstructor
public class JpaCountryDao implements CountryDao {
    private final CountryRepository countryRepository;

    @Override
    public List<Country> findByMovieId(int movieId) {
        return countryRepository.findAllByMovieId(movieId);
    }

    @Override
    public List<Country> findAll() {
        return countryRepository.findAll();
    }
}
