package com.tsa.movieland.service;

import com.tsa.movieland.cache.CachedMovies;
import com.tsa.movieland.currency.CurrencyExchangeService;
import com.tsa.movieland.currency.CurrencyType;
import com.tsa.movieland.dao.MovieDao;
import com.tsa.movieland.common.*;
import com.tsa.movieland.dto.AddUpdateMovieDto;
import com.tsa.movieland.dto.MovieByIdDto;
import com.tsa.movieland.entity.MovieFindAllDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class DefaultMovieService implements MovieService {
    private final static CachedMovies CACHED_MOVIES = new CachedMovies();

    private final MovieDao movieDao;
    private final PosterService posterService;
    private final RatingService ratingService;
    private final MovieEnrichmentService movieEnrichmentService;
    private final CurrencyExchangeService exchangeHolder;

    @Override
    public Iterable<MovieFindAllDto> findAll(MovieRequest movieRequest) {
        if (notEmptyMovieRequest(movieRequest)) {
            Iterable<MovieFindAllDto> movies = movieDao.findAll(field(movieRequest), direction(movieRequest));
            return refreshAvgRating(movies);
        }

        return refreshAvgRating(movieDao.findAll());
    }

    private Iterable<MovieFindAllDto> refreshAvgRating(Iterable<MovieFindAllDto> movies) {
        StreamSupport.stream(movies.spliterator(), false).forEach(movie -> movie.setRating(ratingService.getAvgRate(movie.getId())));
        return movies;
    }

    private boolean notEmptyMovieRequest(MovieRequest movieRequest) {
        return Objects.nonNull(movieRequest.getSortField()) && Objects.nonNull(movieRequest.getSortDirection());
    }

    private String field(MovieRequest defaultMovieRequest) {
        SortField sortField = defaultMovieRequest.getSortField();
        return sortField.getColumnName();
    }

    private String direction(MovieRequest defaultMovieRequest) {
        SortDirection sortDirection = defaultMovieRequest.getSortDirection();
        return sortDirection.toString();
    }

    @Override
    public Iterable<MovieFindAllDto> findRandom() {
        return refreshAvgRating(movieDao.findRandom());
    }

    @Override
    public Iterable<MovieFindAllDto> findByGenre(int genreId, MovieRequest movieRequest) {
        if (notEmptyMovieRequest(movieRequest)) {
            return movieDao.findByGenreId(genreId, field(movieRequest), direction(movieRequest));
        }
        return refreshAvgRating(movieDao.findByGenreId(genreId));
    }

    @Override
    public MovieByIdDto getById(int movieId, MovieRequest movieRequest) {
        MovieByIdDto foundMovie = CACHED_MOVIES.getMovie(movieId, () -> getEnrichedMovie(movieId));
        CurrencyType currencyType = movieRequest.getCurrencyType();
        return createResponse(currencyType, foundMovie);
    }

    private MovieByIdDto getEnrichedMovie(int movieId) {
        return movieEnrichmentService.enrich(movieId, () -> movieDao.findById(movieId));
    }

    private MovieByIdDto createResponse(CurrencyType currency, MovieByIdDto movie) {
        movie.setRating(ratingService.getAvgRate(movie.getId()));
        MovieByIdDto movieCopy = copy(movie);
        calculatePrice(currency, movieCopy);

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
        if (Objects.nonNull(currency)) {
            double price = movie.getPrice();
            double excange = exchangeHolder.excange(currency, price);
            movie.setPrice(excange);
        }
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
