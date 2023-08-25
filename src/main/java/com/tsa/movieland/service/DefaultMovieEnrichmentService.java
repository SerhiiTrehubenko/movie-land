package com.tsa.movieland.service;

import com.tsa.movieland.dto.MovieByIdDto;
import com.tsa.movieland.entity.Country;
import com.tsa.movieland.entity.Genre;
import com.tsa.movieland.entity.Review;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class DefaultMovieEnrichmentService implements MovieEnrichmentService {

    private final static ExecutorService THREAD_POOL = Executors.newCachedThreadPool();
    private final CountryService countryService;
    private final GenreService genreService;
    private final ReviewService reviewService;

    @Override
    public MovieByIdDto enrich(MovieByIdDto movie) {
        int movieId = movie.getId();

        Future<Iterable<Country>> taskCountry = THREAD_POOL.submit(() -> countryService.findByMovieId(movieId));
        Future<Iterable<Genre>> taskGenres = THREAD_POOL.submit(() -> genreService.findByMovieId(movieId));
        Future<Iterable<Review>> taskReview = THREAD_POOL.submit(() -> reviewService.findByMovieId(movieId));

        movie.setCountries(getResult(taskCountry));
        movie.setGenres(getResult(taskGenres));
        movie.setReviews(getResult(taskReview));

        return movie;
    }

    @SneakyThrows
    private <V> V getResult(Future<V> task) {
        return task.get(5, TimeUnit.SECONDS);
    }
}
