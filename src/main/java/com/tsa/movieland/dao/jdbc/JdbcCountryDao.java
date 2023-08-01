package com.tsa.movieland.dao.jdbc;

import com.tsa.movieland.dao.CountryDao;
import com.tsa.movieland.dao.jdbc.mapper.CountryMapper;
import com.tsa.movieland.entity.Country;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@RequiredArgsConstructor
public class JdbcCountryDao implements CountryDao {

    private final CountryMapper countryMapper;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Iterable<Country> findByMovieId(int movieId) {
        String queryCountries = "SELECT country_id, country_name FROM countries_by_movie_id " +
                "WHERE movie_id = :movieId";
        return namedParameterJdbcTemplate.query(
                queryCountries,
                Map.of("movieId", movieId),
                countryMapper
        );
    }
}
