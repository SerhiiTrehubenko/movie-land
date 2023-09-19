package com.tsa.movieland.service;

import com.tsa.movieland.common.AsyncExecutor;
import com.tsa.movieland.dto.GenreDto;
import com.tsa.movieland.dto.MovieByIdDto;
import com.tsa.movieland.entity.Country;
import com.tsa.movieland.entity.Review;
import com.tsa.movieland.exception.MovieEnrichmentException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;
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
        CompletableFuture<Iterable<Country>> countriesTask = asyncExecutor.executeTask(() -> countryService.findByMovieId(movieId));
        CompletableFuture<Iterable<GenreDto>> genresTask = asyncExecutor.executeTask(() -> genreService.findByMovieId(movieId));
        CompletableFuture<Iterable<Review>> reviewsTask = asyncExecutor.executeTask(() -> reviewService.findByMovieId(movieId));

        MovieByIdDto movie = getMovieResult(movieTask);
        movie.setCountries(getResult(countriesTask, Country.class));
        movie.setGenres(getResult(genresTask, GenreDto.class));
        movie.setReviews(getResult(reviewsTask, Review.class));

        return movie;
    }

    private MovieByIdDto getMovieResult(Future<MovieByIdDto> task) {
        try {
            return task.get();
        } catch (Exception e) {
            throw new MovieEnrichmentException("During execution a fetching Movie task occurred an Exception: [%s]".formatted(e.getClass().getCanonicalName()));
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
