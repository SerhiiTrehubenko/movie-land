package com.tsa.movieland.controller;

import com.tsa.movieland.common.MovieRequest;
import com.tsa.movieland.dto.AddUpdateMovieDto;
import com.tsa.movieland.dto.MovieByIdDto;
import com.tsa.movieland.entity.MovieFindAllDto;
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
    private final PosterService posterService;

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
        int movieId = movieService.save(movie);
        posterService.add(movieId, movie.getPicturePath());
        log.info("Query add a movie");
    }

    @PutMapping("/{id}")
    public void  updateMovie(@PathVariable("id") int movieId, @RequestBody AddUpdateMovieDto movie) {
        movieService.update(movieId, movie);
        posterService.update(movieId, movie.getPicturePath());
        log.info("Query update a movie");
    }
}
