package com.tsa.movieland.dao.jpa;

import com.tsa.movieland.context.JpaDao;
import com.tsa.movieland.dao.MovieDao;
import com.tsa.movieland.dao.jpa.repository.MovieRepository;
import com.tsa.movieland.entity.Movie;
import com.tsa.movieland.exception.MovieNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@JpaDao
@RequiredArgsConstructor
public class JpaMovieDao implements MovieDao {
    @Value("${number.movies.random}")
    private Integer randomQuantity;
    private final MovieRepository movieRepository;

    @Override
    public List<Movie> findAll() {
        return movieRepository.findAllByOrderByIdAsc();
    }

    @Override
    @Transactional
    public List<Movie> findRandom() {
        return movieRepository.findRandom(PageRequest.of(
                0, randomQuantity
                ));
    }

    @Override
    public List<Movie> findByGenreId(int genreId) {
        return movieRepository.findByGenreId(genreId);
    }

    @Override
    public Movie findById(int movieId) {
        return movieRepository.findByIdOrderById(movieId)
                .orElseThrow(() -> this.throwMovieNotFoundException(movieId));
    }

    @Override
    public Movie findByIdForUpdate(int movieId) {
        return movieRepository.findById(movieId).orElseThrow(() -> this.throwMovieNotFoundException(movieId));
    }

    private MovieNotFoundException throwMovieNotFoundException(int movieId) {
        return new MovieNotFoundException("MovieDto with id: [%d] is absent".formatted(movieId));
    }

    @Override
    @Transactional
    public Movie save(Movie movie) {
        return movieRepository.save(movie);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Movie> findAllById(List<Integer> foundIds) {
        return movieRepository.findAllById(foundIds);
    }
}
