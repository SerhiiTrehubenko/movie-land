package com.tsa.movieland.dao.jpa;

import com.tsa.movieland.context.JpaDao;
import com.tsa.movieland.dao.CountryDao;
//import com.tsa.movieland.dao.MovieDao;
import com.tsa.movieland.dto.CountryDto;
import com.tsa.movieland.mapper.CountryMapper;
import com.tsa.movieland.repository.CountryRepository;
import lombok.RequiredArgsConstructor;

@JpaDao
@RequiredArgsConstructor
public class JpaCountryDao implements CountryDao {
    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;

    @Override
    public Iterable<CountryDto> findByMovieId(int movieId) {
        return countryRepository.findAllByMovieId(movieId).stream()
                .map(countryMapper::toCountryDto).toList();
    }

    @Override
    public Iterable<CountryDto> findAll() {
        return countryRepository.findAll().stream()
                .map(countryMapper::toCountryDto)
                .toList();
    }


}
