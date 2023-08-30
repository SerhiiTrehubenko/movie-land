package com.tsa.movieland.service;

import com.tsa.movieland.common.AsyncExecutor;
import com.tsa.movieland.dto.MovieByIdDto;
import com.tsa.movieland.entity.Country;
import com.tsa.movieland.entity.Genre;
import com.tsa.movieland.entity.Review;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@Profile("dev-async")
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
        CompletableFuture<Iterable<Genre>> genresTask = asyncExecutor.executeTask(() -> genreService.findByMovieId(movieId));
        CompletableFuture<Iterable<Review>> reviewsTask = asyncExecutor.executeTask(() -> reviewService.findByMovieId(movieId));

        MovieByIdDto movie = movieTask.get();
        movie.setCountries(countriesTask.get());
        movie.setGenres(genresTask.get());
        movie.setReviews(reviewsTask.get());

        return movie;
    }
}
