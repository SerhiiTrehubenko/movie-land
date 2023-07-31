package com.tsa.movieland.controller;

import com.tsa.movieland.common.MovieRequest;
import com.tsa.movieland.entity.Movie;
import com.tsa.movieland.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/movie", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

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
}
