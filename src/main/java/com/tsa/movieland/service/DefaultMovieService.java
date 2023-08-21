package com.tsa.movieland.service;

import com.tsa.movieland.dao.MovieDao;
import com.tsa.movieland.common.*;
import com.tsa.movieland.dto.AddUpdateMovieDto;
import com.tsa.movieland.dto.MovieByIdDto;
import com.tsa.movieland.entity.MovieFindAllDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DefaultMovieService implements MovieService {
    private final MovieDao movieDao;

    @Override
    public Iterable<MovieFindAllDto> findAll(MovieRequest movieRequest) {
        if (notEmptyMovieRequest(movieRequest)) {
            return movieDao.findAll(field(movieRequest), direction(movieRequest));
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
    public Iterable<MovieFindAllDto> findByGenre(int genreId, MovieRequest movieRequest) {
        if (notEmptyMovieRequest(movieRequest)) {
            return movieDao.findByGenreId(genreId, field(movieRequest), direction(movieRequest));
        }
        return movieDao.findByGenreId(genreId);
    }

    @Override
    public MovieByIdDto getById(int movieId, MovieRequest movieRequest) {
        return movieDao.findById(movieId);
    }

    @Override
    public int save(AddUpdateMovieDto movie) {
        return movieDao.save(movie);
    }

    @Override
    public void update(int movieId, AddUpdateMovieDto movie) {
        movieDao.update(movieId, movie);
    }
}
