package com.tsa.movieland.controller;

import com.tsa.movieland.common.MovieRequest;
import com.tsa.movieland.dao.PosterDao;
import com.tsa.movieland.dto.AddUpdateMovieDto;
import com.tsa.movieland.dto.MovieByIdDto;
import com.tsa.movieland.entity.Country;
import com.tsa.movieland.entity.Genre;
import com.tsa.movieland.entity.MovieFindAllDto;
import com.tsa.movieland.entity.Review;
import com.tsa.movieland.service.*;
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
    private final PosterDao posterDao;
    private final ThreadExecutor executor;

    @GetMapping()
    public Iterable<MovieFindAllDto> findAll(MovieRequest movieRequest) {
        return movieService.findAll(movieRequest);
    }

    @GetMapping("/random")
    public Iterable<MovieFindAllDto> random() {
        return movieService.findRandom();
    }

    @GetMapping("/genre/{genreId}")
    public Iterable<MovieFindAllDto> findByGenreSorted(@PathVariable("genreId") int genreId,
                                                       MovieRequest movieRequest) {
        return movieService.findByGenre(genreId, movieRequest);
    }

    @GetMapping("/{movieId}")
    public MovieByIdDto getById(@PathVariable int movieId,
                                MovieRequest movieRequest) {
        processRequest(movieId, movieRequest);
        return getMovieByIdDto();
    }

    private void processRequest(int movieId, MovieRequest movieRequest) {
        executor.addMethodTwoParam(movieService::getById, movieId, movieRequest);
        executor.addMethodOneParam(countryService::findByMovieId, movieId);
        executor.addMethodOneParam(genreService::findByMovieId, movieId);
        executor.addMethodOneParam(reviewService::findByMovieId, movieId);

    }

    private MovieByIdDto getMovieByIdDto() {
        final ResultExtractor resultExtractor = executor.executeTasks();
        final MovieByIdDto result = resultExtractor.getObject(MovieByIdDto.class);
        result.setCountries(resultExtractor.getList(Country.class));
        result.setGenres(resultExtractor.getList(Genre.class));
        result.setReviews(resultExtractor.getList(Review.class));
        return result;
    }

    @PostMapping
    public void addMovie(@RequestBody AddUpdateMovieDto movie) {
        final int movieId = movieService.save(movie);
        posterDao.addPoster(movieId, movie.getPicturePath());
    }
}
