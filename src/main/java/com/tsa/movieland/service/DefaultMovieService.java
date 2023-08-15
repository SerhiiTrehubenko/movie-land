package com.tsa.movieland.service;

import com.tsa.movieland.currency.CurrencyExchangeHolder;
import com.tsa.movieland.currency.CurrencyType;
import com.tsa.movieland.dao.MovieDao;
import com.tsa.movieland.common.*;
import com.tsa.movieland.dto.AddUpdateMovieDto;
import com.tsa.movieland.dto.MovieByIdDto;
import com.tsa.movieland.entity.MovieFindAllDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DefaultMovieService implements MovieService {
    private final MovieDao movieDao;
    private final CurrencyExchangeHolder exchangeHolder;

    @Override
    public Iterable<MovieFindAllDto> findAll(MovieRequest defaultMovieRequest) {
        if (notEmptyMovieRequest(defaultMovieRequest)) {
            return movieDao.findAll(field(defaultMovieRequest), direction(defaultMovieRequest));
        }
        return movieDao.findAll();
    }

    private boolean notEmptyMovieRequest(MovieRequest movieRequest) {
        return Objects.nonNull(movieRequest.getSortField()) && Objects.nonNull(movieRequest.getSortDirection());
    }

    private String field(MovieRequest defaultMovieRequest) {
        SortField sortField = defaultMovieRequest.getSortField();
        return sortField.getColumnName();
    }

    private String direction(MovieRequest defaultMovieRequest) {
        SortDirection sortDirection = defaultMovieRequest.getSortDirection();
        return sortDirection.toString();
    }

    @Override
    public Iterable<MovieFindAllDto> findRandom() {
        return movieDao.findRandom();
    }

    @Override
    public Iterable<MovieFindAllDto> findByGenre(int genreId, MovieRequest defaultMovieRequest) {
        if (notEmptyMovieRequest(defaultMovieRequest)) {
            return movieDao.findByGenreId(genreId, field(defaultMovieRequest), direction(defaultMovieRequest));
        }
        return movieDao.findByGenreId(genreId);
    }

    @Override
    public MovieByIdDto getById(int movieId, MovieRequest movieRequest) {
        CurrencyType currencyType = movieRequest.getCurrencyType();
        if (Objects.nonNull(currencyType)) {
            MovieByIdDto movie = movieDao.findById(movieId);
            double convertedPrice = convertPrice(movie.getPrice(), exchangeHolder.getRating(currencyType));
            movie.setPrice(convertedPrice);
            return movie;
        }
        return movieDao.findById(movieId);
    }

    private double convertPrice(double dividend, double divider) {
        BigDecimal bigDecimal = new BigDecimal(dividend / divider);
        bigDecimal = bigDecimal.setScale(2, RoundingMode.CEILING);
        return bigDecimal.doubleValue();
    }

    @Override
    public int save(AddUpdateMovieDto movie) {
        return movieDao.save(movie);
    }
}
