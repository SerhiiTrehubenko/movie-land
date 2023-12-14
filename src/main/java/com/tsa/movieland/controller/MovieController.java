package com.tsa.movieland.controller;

import com.tsa.movieland.common.MovieRequest;
import com.tsa.movieland.common.RatingRequest;
import com.tsa.movieland.dto.AddUpdateMovieDto;
import com.tsa.movieland.dto.MovieByIdDto;
import com.tsa.movieland.dto.MovieFindAllDto;
import com.tsa.movieland.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/movie", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class MovieController {

    private final MovieService movieService;

    @GetMapping()
    public Iterable<MovieFindAllDto> findAll(MovieRequest movieRequest) {
        log.info("Query get all movies");
        return movieService.findAll(movieRequest);
    }

    @GetMapping("/random")
    public Iterable<MovieFindAllDto> random() {
        log.info("Query get random movies");
        return movieService.findRandom();
    }

    @GetMapping("/genre/{genreId}")
    public Iterable<MovieFindAllDto> findByGenreSorted(@PathVariable("genreId") int genreId,
                                                       MovieRequest movieRequest) {
        log.info("Query get a movie by genre id: [{}]", genreId);
        return movieService.findByGenre(genreId, movieRequest);
    }

    @GetMapping("/{movieId}")
    public MovieByIdDto getById(@PathVariable int movieId,
                                MovieRequest movieRequest) {
        log.info("Query get a movie by id: [{}]", movieId);
        return movieService.getById(movieId, movieRequest);
    }

    @PostMapping
    public void addMovie(@RequestBody AddUpdateMovieDto movie) {
        movieService.save(movie);
        log.info("Query add a movie");
    }

    @PutMapping("/{id}")
    public void  updateMovie(@PathVariable("id") int movieId, @RequestBody AddUpdateMovieDto movie) {
        movieService.update(movieId, movie);
        log.info("Query update a movie");
    }

    @PostMapping("/{movieId}/rate")
    public void rateMovie(RatingRequest ratingRequest) {
        movieService.addRating(ratingRequest);
        log.info("rating was added for movie: [{}]", ratingRequest.getMovieId());
    }

    @GetMapping("/search/{title}")
    public Iterable<MovieFindAllDto> search(@PathVariable String title) {
        return movieService.search(title);
    }
}
