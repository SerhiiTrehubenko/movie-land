package com.tsa.movieland.service;

import com.tsa.movieland.dao.MovieDao;
import com.tsa.movieland.util.*;
import com.tsa.movieland.entity.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultMovieService implements MovieService {
    private final MovieDao movieDao;

    @Override
    public Iterable<Movie> findAllSorted(MovieRequest defaultMovieRequest) {
        if (notEmptyEmptyMovieRequest(defaultMovieRequest)) {
            return movieDao.findAllSorted(field(defaultMovieRequest), direction(defaultMovieRequest));
        }
        return movieDao.findAll();
    }

    private boolean notEmptyEmptyMovieRequest(MovieRequest defaultMovieRequest) {
        return !EmptyMovieRequest.class.isAssignableFrom(defaultMovieRequest.getClass());
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
    public Iterable<Movie> findByGenreSorted(int genreId, MovieRequest defaultMovieRequest) {
        if (notEmptyEmptyMovieRequest(defaultMovieRequest)) {
            return movieDao.findByGenreIdSorted(genreId, field(defaultMovieRequest), direction(defaultMovieRequest));
        }
        return movieDao.findByGenreId(genreId);
    }
}
