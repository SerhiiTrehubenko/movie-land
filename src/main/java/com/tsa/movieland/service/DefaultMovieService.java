package com.tsa.movieland.service;

import com.tsa.movieland.dao.MovieDao;
import com.tsa.movieland.common.*;
import com.tsa.movieland.entity.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DefaultMovieService implements MovieService {
    private final MovieDao movieDao;

    @Override
    public Iterable<Movie> findAll(MovieRequest defaultMovieRequest) {
        if (notEmptyEmptyMovieRequest(defaultMovieRequest)) {
            return movieDao.findAll(field(defaultMovieRequest), direction(defaultMovieRequest));
        }
        return movieDao.findAll();
    }

    private boolean notEmptyEmptyMovieRequest(MovieRequest movieRequest) {
        return Objects.nonNull(movieRequest.getSortField()) && Objects.nonNull(movieRequest.getSortDirection()) ;
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
    public Iterable<Movie> findRandom() {
        return movieDao.findRandom();
    }

    @Override
    public Iterable<Movie> findByGenre(int genreId, MovieRequest defaultMovieRequest) {
        if (notEmptyEmptyMovieRequest(defaultMovieRequest)) {
            return movieDao.findByGenreId(genreId, field(defaultMovieRequest), direction(defaultMovieRequest));
        }
        return movieDao.findByGenreId(genreId);
    }
}
