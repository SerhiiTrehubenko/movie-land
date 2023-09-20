package com.tsa.movieland.service;

import com.tsa.movieland.dao.CountryDao;
import com.tsa.movieland.dto.CountryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultCountryService implements CountryService {

    private final CountryDao countryDao;

    @Override
    public Iterable<CountryDto> findByMovieId(int movieId) {
        return countryDao.findByMovieId(movieId);
    }

    @Override
    public Iterable<CountryDto> findAll() {
        return countryDao.findAll();
    }
}
