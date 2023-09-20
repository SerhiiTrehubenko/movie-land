package com.tsa.movieland.service;

import com.tsa.movieland.dto.GenreDto;
import com.tsa.movieland.dto.MovieByIdDto;
import com.tsa.movieland.dto.CountryDto;
import com.tsa.movieland.dto.ReviewDto;
import com.tsa.movieland.exception.MovieEnrichmentException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.concurrent.*;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@Profile("dev-threads")
@Slf4j
public class DefaultMovieEnrichmentService implements MovieEnrichmentService {

    private final ExecutorService threadPool;
    private final CountryService countryService;
    private final GenreService genreService;
    private final ReviewService reviewService;

    @Override
    @SneakyThrows
    public MovieByIdDto enrich(int movieId, Supplier<MovieByIdDto> movieSupplier) {

        Future<MovieByIdDto> movieResult = threadPool.submit(movieSupplier::get);
        Future<Iterable<CountryDto>> countryResult = threadPool.submit(() -> getTaskResult(() -> countryService.findByMovieId(movieId), CountryDto.class));
        Future<Iterable<GenreDto>> genresResult = threadPool.submit(() -> getTaskResult(() -> genreService.findByMovieId(movieId), GenreDto.class));
        Future<Iterable<ReviewDto>> reviewResult = threadPool.submit(() -> getTaskResult(() -> reviewService.findByMovieId(movieId), ReviewDto.class));

        MovieByIdDto foundMovie = getMovieResult(movieResult);
        foundMovie.setCountries(countryResult.get());
        foundMovie.setGenres(genresResult.get());
        foundMovie.setReviews(reviewResult.get());

        return foundMovie;
    }

    private MovieByIdDto getMovieResult(Future<MovieByIdDto> task) {
        try {
            return task.get(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new MovieEnrichmentException("During execution a fetching MovieDto task occurred an Exception: [%s]".formatted(e.getClass().getCanonicalName()));
        }
    }

    private <T> T getTaskResult(Callable<T> task, Class<?> resultClass) {
        try {
            Future<T> submit = threadPool.submit(task);
            return submit.get(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.info("During execution a task of: [{}] there was an exception: \n Cause: [{}]",
                    resultClass.getSimpleName(), e.getClass().getCanonicalName());
            return (T) Collections.EMPTY_LIST;
        }
    }
}
