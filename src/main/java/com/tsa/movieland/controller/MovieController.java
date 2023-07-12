package com.tsa.movieland.controller;

import com.tsa.movieland.domain.SortDirection;
import com.tsa.movieland.entity.Movie;
import com.tsa.movieland.service.MovieService;
import com.tsa.movieland.service.SortDirectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/movie", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;
    private final SortDirectionService sortDirectionService;

    @GetMapping()
    public Iterable<Movie> findAll(@RequestParam(required = false) Map<String, String> params) {
        SortDirection sortDirection = sortDirectionService.getSortDirection(params);
        return movieService.findAll(sortDirection);
    }

    @GetMapping("/random")
    public Iterable<Movie> threeRandom() {
        return movieService.findThreeRandom();
    }

    @GetMapping("/genre/{genreId}")
    public Iterable<Movie> findByGenreSortByRating(@PathVariable("genreId") int genreId,
                                                   @RequestParam(required = false) Map<String, String> params) {
        SortDirection sortDirection = sortDirectionService.getSortDirection(params);
        return movieService.findByGenre(genreId, sortDirection);
    }
}
