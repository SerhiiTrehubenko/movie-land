package com.tsa.movieland.service;

import com.tsa.movieland.common.AsyncExecutor;
import com.tsa.movieland.dto.GenreDto;
import com.tsa.movieland.dto.MovieByIdDto;
import com.tsa.movieland.dto.CountryDto;
import com.tsa.movieland.dto.ReviewDto;
import com.tsa.movieland.exception.MovieEnrichmentException;
import com.tsa.movieland.exception.MovieNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@Profile("dev-async")
@Slf4j
public class AsyncMovieEnrichmentService implements MovieEnrichmentService {
    private final AsyncExecutor asyncExecutor;
    private final CountryService countryService;
    private final GenreService genreService;
    private final ReviewService reviewService;
    @Override
    @SneakyThrows
    public MovieByIdDto enrich(int movieId, Supplier<MovieByIdDto> supplier) {
        CompletableFuture<MovieByIdDto> movieTask = asyncExecutor.executeTask(supplier);
        CompletableFuture<Iterable<CountryDto>> countriesTask = asyncExecutor.executeTask(() -> countryService.findByMovieId(movieId));
        CompletableFuture<Iterable<GenreDto>> genresTask = asyncExecutor.executeTask(() -> genreService.findByMovieId(movieId));
        CompletableFuture<Iterable<ReviewDto>> reviewsTask = asyncExecutor.executeTask(() -> reviewService.findByMovieId(movieId));

        MovieByIdDto movie = getMovieResult(movieTask);
        movie.setCountries(getResult(countriesTask, CountryDto.class));
        movie.setGenres(getResult(genresTask, GenreDto.class));
        movie.setReviews(getResult(reviewsTask, ReviewDto.class));

        return movie;
    }

    private MovieByIdDto getMovieResult(Future<MovieByIdDto> task) {
        try {
            return task.get();
        } catch (InterruptedException | CancellationException | ExecutionException e) {
            final Throwable cause = e.getCause();
            if (Objects.nonNull(cause) && MovieNotFoundException.class.isAssignableFrom(cause.getClass())) {
                throw (MovieNotFoundException) cause;
            }
            throw new MovieEnrichmentException("During execution a fetching MovieDto task occurred an Exception: [%s]".formatted(e.getMessage()));
        }
    }

    private <T> T getResult(Future<T> task, Class<?> resultClass) {
        try {
            return task.get();
        } catch (Exception e) {
            log.info("During execution a task of: [{}] there was an exception: \n Cause: [{}]",
                    resultClass.getSimpleName(), e.getClass().getCanonicalName());
            return (T) Collections.EMPTY_LIST;
        }
    }
}
