package com.tsa.movieland.controller;

import com.tsa.movieland.entity.Movie;
import com.tsa.movieland.service.MovieService;
import com.tsa.movieland.util.MovieRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/movie", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping()
    public Iterable<Movie> findAll(MovieRequest defaultMovieRequest) {
        return movieService.findAllSorted(defaultMovieRequest);
    }

    @GetMapping("/random")
    public Iterable<Movie> threeRandom() {
        return movieService.findRandom();
    }

    @GetMapping("/genre/{genreId}")
    public Iterable<Movie> findByGenreSorted(@PathVariable("genreId") int genreId,
                                             MovieRequest defaultMovieRequest) {
        return movieService.findByGenreSorted(genreId, defaultMovieRequest);
    }
}
