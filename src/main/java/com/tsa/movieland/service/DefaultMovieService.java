package com.tsa.movieland.service;

import com.tsa.movieland.dao.MovieDao;
import com.tsa.movieland.domain.MovieRequest;
import com.tsa.movieland.domain.SortDirection;
import com.tsa.movieland.domain.SortField;
import com.tsa.movieland.entity.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultMovieService implements MovieService {
    private final MovieDao movieDao;

    @Override
    public Iterable<Movie> findAllSorted(MovieRequest movieRequest) {
        if (notEmptyEmptyMovieRequest(movieRequest)) {
            return movieDao.findAllSorted(field(movieRequest), direction(movieRequest));
        }
        return movieDao.findAll();
    }

    private boolean notEmptyEmptyMovieRequest(MovieRequest movieRequest) {
        return !MovieRequest.EmptyMovieRequest.class.isAssignableFrom(movieRequest.getClass());
    }

    private String field(MovieRequest movieRequest) {
        SortField sortField = movieRequest.getSortField();
        return sortField.getColumnName();
    }

    private String direction(MovieRequest movieRequest) {
        SortDirection sortDirection = movieRequest.getSortDirection();
        return sortDirection.toString();
    }

    @Override
    public Iterable<Movie> findRandom() {
        return movieDao.findRandom();
    }

    @Override
    public Iterable<Movie> findByGenreSorted(int genreId, MovieRequest movieRequest) {
        if (notEmptyEmptyMovieRequest(movieRequest)) {
            return movieDao.findByGenreIdSorted(genreId, field(movieRequest), direction(movieRequest));
        }
        return movieDao.findByGenreId(genreId);
    }
}
