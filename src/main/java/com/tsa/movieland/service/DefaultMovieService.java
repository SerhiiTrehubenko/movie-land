package com.tsa.movieland.service;

import com.tsa.movieland.cache.CachedMovies;
import com.tsa.movieland.currency.CurrencyExchangeService;
import com.tsa.movieland.currency.CurrencyType;
import com.tsa.movieland.dao.MovieDao;
import com.tsa.movieland.common.*;
import com.tsa.movieland.dto.AddUpdateMovieDto;
import com.tsa.movieland.dto.MovieByIdDto;
import com.tsa.movieland.dto.MovieFindAllDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class DefaultMovieService implements MovieService {
    private final Map<Boolean, BiConsumer<CurrencyType, MovieByIdDto>> calculateCurrency = Map.of(
            Boolean.TRUE, this::calculatePrice,
            Boolean.FALSE, (currency, movie) -> {
//                "do nothing"
            }
    );
    private final static CachedMovies CACHED_MOVIES = new CachedMovies();
    private final static MovieSorter MOVIE_SORTER = new MovieSorter();

    private final MovieDao movieDao;
    private final PosterService posterService;
    private final RatingService ratingService;
    private final MovieEnrichmentService movieEnrichmentService;
    private final CurrencyExchangeService exchangeHolder;

    @Override
    @Transactional(readOnly = true)
    public Iterable<MovieFindAllDto> findAll(MovieRequest movieRequest) {
        List<MovieFindAllDto> movies = (List<MovieFindAllDto>) refreshAvgRating(movieDao.findAll());
        MOVIE_SORTER.sort(movies, movieRequest);
        return movies;
    }

    private Iterable<MovieFindAllDto> refreshAvgRating(Iterable<MovieFindAllDto> movies) {
        StreamSupport.stream(movies.spliterator(), false).forEach(movie -> movie.setRating(ratingService.getAvgRate(movie.getId())));
        return movies;
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<MovieFindAllDto> findRandom() {
        return refreshAvgRating(movieDao.findRandom());
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<MovieFindAllDto> findByGenre(int genreId, MovieRequest movieRequest) {
        List<MovieFindAllDto> movies = (List<MovieFindAllDto>) movieDao.findByGenreId(genreId);
        refreshAvgRating(movies);

        MOVIE_SORTER.sort(movies, movieRequest);
        return movies;
    }

    @Override
    @Transactional(readOnly = true)
    public MovieByIdDto getById(int movieId, MovieRequest movieRequest) {
        MovieByIdDto movie = CACHED_MOVIES.getMovie(movieId, () -> getEnrichedMovie(movieId));
        CurrencyType currency = movieRequest.getCurrencyType();
        return createResponse(currency, movie);
    }

    private MovieByIdDto getEnrichedMovie(int movieId) {
        return movieEnrichmentService.enrich(movieId, () -> movieDao.findById(movieId));
    }

    private MovieByIdDto createResponse(CurrencyType currency, MovieByIdDto movie) {
        movie.setRating(ratingService.getAvgRate(movie.getId()));
        MovieByIdDto movieCopy = copy(movie);

        calculateCurrency.get(Objects.nonNull(currency)).accept(currency, movieCopy);
        return movieCopy;
    }

    private MovieByIdDto copy(MovieByIdDto movie) {
        return MovieByIdDto.builder()
                .id(movie.getId())
                .nameRussian(movie.getNameRussian())
                .nameNative(movie.getNameNative())
                .yearOfRelease(movie.getYearOfRelease())
                .description(movie.getDescription())
                .rating(movie.getRating())
                .price(movie.getPrice())
                .picturePath(movie.getPicturePath())
                .countries(movie.getCountries())
                .genres(movie.getGenres())
                .reviews(movie.getReviews())
                .build();
    }

    private void calculatePrice(CurrencyType currency, MovieByIdDto movie) {
        double price = movie.getPrice();
        double excange = exchangeHolder.excange(currency, price);
        movie.setPrice(excange);
    }

    @Override
    @Transactional
    public int save(AddUpdateMovieDto movie) {
        int movieId = movieDao.save(movie);
        posterService.add(movieId, movie.getPicturePath());
        return movieId;
    }

    @Override
    @Transactional
    public void update(int movieId, AddUpdateMovieDto movie) {
        movieDao.update(movieId, movie);
        posterService.update(movieId, movie.getPicturePath());
        CACHED_MOVIES.removeFromCache(movieId);
    }

    @Override
    public void addRating(RatingRequest ratingRequest) {
        ratingService.addRating(ratingRequest);
    }
}
