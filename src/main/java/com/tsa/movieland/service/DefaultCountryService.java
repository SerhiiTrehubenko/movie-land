package com.tsa.movieland.service;

import com.tsa.movieland.dao.CountryDao;
import com.tsa.movieland.entity.Country;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultCountryService implements CountryService {

    private final CountryDao countryDao;

    @Override
    public Iterable<Country> findByMovieId(int movieId) {
        return countryDao.findByMovieId(movieId);
    }

    @Override
    public Iterable<Country> findAll() {
        return countryDao.findAll();
    }
}
