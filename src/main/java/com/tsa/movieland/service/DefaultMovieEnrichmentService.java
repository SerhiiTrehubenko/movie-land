package com.tsa.movieland.service;

import com.tsa.movieland.dto.MovieByIdDto;
import com.tsa.movieland.entity.Country;
import com.tsa.movieland.entity.Genre;
import com.tsa.movieland.entity.Review;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@Profile("dev-threads")
public class DefaultMovieEnrichmentService implements MovieEnrichmentService {

    private final AsyncConfigurer threadPool;
    private final CountryService countryService;
    private final GenreService genreService;
    private final ReviewService reviewService;

    @Override
    @SneakyThrows
    public MovieByIdDto enrich(int movieId, Supplier<MovieByIdDto> movieSupplier) {
        ExecutorService executor = (ExecutorService) threadPool.getAsyncExecutor();
        Objects.requireNonNull(executor);
        Future<MovieByIdDto> taskMovie = executor.submit(movieSupplier::get);
        Future<Iterable<Country>> taskCountries = executor.submit(() -> countryService.findByMovieId(movieId));
        Future<Iterable<Genre>> taskGenres = executor.submit(() -> genreService.findByMovieId(movieId));
        Future<Iterable<Review>> taskReviews = executor.submit(() -> reviewService.findByMovieId(movieId));

        Future<MovieByIdDto> movieResult = executor.submit(() -> getTaskResult(taskMovie));
        Future<Iterable<Country>> countryResult = executor.submit(() -> getTaskResult(taskCountries));
        Future<Iterable<Genre>> genresResult = executor.submit(() -> getTaskResult(taskGenres));
        Future<Iterable<Review>> reviewResult = executor.submit(() -> getTaskResult(taskReviews));

        MovieByIdDto foundMovie = Objects.requireNonNull(movieResult.get());
        foundMovie.setCountries(countryResult.get());
        foundMovie.setGenres(genresResult.get());
        foundMovie.setReviews(reviewResult.get());

        return foundMovie;
    }

    @SneakyThrows
    private <V> V getTaskResult(Future<V> task) {
        return task.get(5, TimeUnit.SECONDS);
    }
}
