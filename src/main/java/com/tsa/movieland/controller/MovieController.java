package com.tsa.movieland.controller;

import com.tsa.movieland.entity.Movie;
import com.tsa.movieland.service.MovieService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/movie", produces = MediaType.APPLICATION_JSON_VALUE)
public class MovieController {


    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping()
    public List<Movie> findAll(@RequestParam(value = "rating", required = false) String rating,
                               @RequestParam(value = "price", required = false) String price) {
        return movieService.findAll(rating, price);
    }

    @GetMapping("/random")
    public List<Movie> threeRandom() {
        return movieService.findThreeRandom();
    }

    @GetMapping("/genre/{genreId}")
    public List<Movie> findByGenreSortByRating(@PathVariable("genreId") int genreId,
                                               @RequestParam(value = "rating", required = false) String rating,
                                               @RequestParam(value = "price", required = false) String price) {
        return movieService.findByGenre(genreId, rating, price);
    }
}
