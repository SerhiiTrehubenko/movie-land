package com.tsa.movieland.controller;

import com.tsa.movieland.common.MovieRequest;
import com.tsa.movieland.dto.MovieByIdDto;
import com.tsa.movieland.entity.Movie;
import com.tsa.movieland.service.CountryService;
import com.tsa.movieland.service.GenreService;
import com.tsa.movieland.service.MovieService;
import com.tsa.movieland.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/movie", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;
    private final CountryService countryService;
    private final GenreService genreService;
    private final ReviewService reviewService;

    @GetMapping()
    public Iterable<Movie> findAll(MovieRequest movieRequest) {
        return movieService.findAll(movieRequest);
    }

    @GetMapping("/random")
    public Iterable<Movie> random() {
        return movieService.findRandom();
    }

    @GetMapping("/genre/{genreId}")
    public Iterable<Movie> findByGenreSorted(@PathVariable("genreId") int genreId,
                                             MovieRequest movieRequest) {
        return movieService.findByGenre(genreId, movieRequest);
    }

    @GetMapping("/{movieId}")
    public MovieByIdDto getById(@PathVariable int movieId,
                                MovieRequest movieRequest) {
        final MovieByIdDto movieByIdDto = movieService.getById(movieId, movieRequest);
        movieByIdDto.setCountries(countryService.findBiMovieId(movieId));
        movieByIdDto.setGenres(genreService.findByMovieId(movieId));
        movieByIdDto.setReviews(reviewService.findBiMovieId(movieId));

        return movieByIdDto;
    }
}
