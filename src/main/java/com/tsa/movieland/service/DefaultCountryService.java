package com.tsa.movieland.service;

import com.tsa.movieland.dao.CountryDao;
import com.tsa.movieland.dto.CountryDto;
import com.tsa.movieland.mapper.CountryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultCountryService implements CountryService {

    private final CountryDao countryDao;
    private final CountryMapper countryMapper;

    @Override
    public Iterable<CountryDto> findByMovieId(int movieId) {
        return countryDao.findByMovieId(movieId).stream()
                .map(countryMapper::toCountryDto).toList();
    }

    @Override
    public Iterable<CountryDto> findAll() {
        return countryDao.findAll().stream()
                .map(countryMapper::toCountryDto).toList();
    }
}
