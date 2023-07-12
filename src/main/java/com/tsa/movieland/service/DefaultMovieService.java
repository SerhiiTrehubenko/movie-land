package com.tsa.movieland.service;

import com.tsa.movieland.dao.MovieDao;
import com.tsa.movieland.domain.SortDirection;
import com.tsa.movieland.entity.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultMovieService implements MovieService {
    private final MovieDao movieDao;

    @Override
    public Iterable<Movie> findAll(SortDirection sortDirection) {
        return sortDirection.findAll(movieDao);
    }

    @Override
    public Iterable<Movie> findThreeRandom() {
        return movieDao.findRandom();
    }

    @Override
    public Iterable<Movie> findByGenre(int genreId, SortDirection sortDirection) {
        return sortDirection.findByGenreId(genreId, movieDao);
    }
}
