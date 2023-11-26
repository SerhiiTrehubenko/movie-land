package com.tsa.movieland.service;

import com.tsa.movieland.dao.CountryDao;
import com.tsa.movieland.dto.CountryDto;
import com.tsa.movieland.entity.Country;
import com.tsa.movieland.mapper.CountryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultCountryService implements CountryService {

    private final CountryDao countryDao;
    private final CountryMapper countryMapper;

    @Override
    public Iterable<CountryDto> findByMovieId(int movieId) {
        return getCountriesDto(countryDao.findByMovieId(movieId));
    }

    @Override
    public Iterable<CountryDto> findAll() {
        return getCountriesDto(countryDao.findAll());
    }

    private Iterable<CountryDto> getCountriesDto(List<Country> countries) {
        return
                countries.stream()
                        .map(countryMapper::toCountryDto)
                        .toList();
    }
}
