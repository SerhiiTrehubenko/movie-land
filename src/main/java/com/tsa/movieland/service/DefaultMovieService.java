package com.tsa.movieland.service;

import com.tsa.movieland.cache.CachedMovies;
import com.tsa.movieland.currency.CurrencyExchangeService;
import com.tsa.movieland.currency.CurrencyType;
import com.tsa.movieland.dao.MovieDao;
import com.tsa.movieland.common.*;
import com.tsa.movieland.dto.AddUpdateMovieDto;
import com.tsa.movieland.dto.MovieByIdDto;
import com.tsa.movieland.dto.MovieFindAllDto;
import com.tsa.movieland.entity.Movie;
import com.tsa.movieland.mapper.MovieMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultMovieService implements MovieService {
    private final static CachedMovies CACHED_MOVIES = new CachedMovies();
    private final static MovieSortCriteria MOVIE_SORTER = new MovieSortCriteria();

    private final MovieDao movieDao;
    private final RatingService ratingService;
    private final MovieEnrichmentService movieEnrichmentService;
    private final CurrencyExchangeService exchangeHolder;
    private final MovieMapper movieMapper;
    private final SearchService searchService;

    @Override
    @Transactional(readOnly = true)
    public Iterable<MovieFindAllDto> findAll(MovieRequest movieRequest) {
        return getDtos(movieDao::findAll, movieRequest);
    }

    private List<MovieFindAllDto> getDtos(Supplier<List<Movie>> supplier, MovieRequest movieRequest) {
        Comparator<MovieFindAllDto> sortCriteria = MOVIE_SORTER.getSortCriteria(movieRequest);
        return supplier.get()
                .stream()
                .map(movieMapper::toMovieFindAllDto)
                .peek(this::updateRating)
                .sorted(sortCriteria)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private void updateRating(MovieFindAllDto dto) {
        dto.setRating(ratingService.getAvgRate(dto.getId()));
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<MovieFindAllDto> findRandom() {
        return movieDao.findRandom()
                .stream()
                .map(movieMapper::toMovieFindAllDto)
                .peek(this::updateRating)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<MovieFindAllDto> findByGenre(int genreId, MovieRequest movieRequest) {
        return getDtos(() -> movieDao.findByGenreId(genreId), movieRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public MovieByIdDto getById(int movieId, MovieRequest movieRequest) {
        MovieByIdDto movie = CACHED_MOVIES.getMovie(movieId, () -> getEnrichedMovie(movieId));
        CurrencyType currency = movieRequest.getCurrencyType();
        return createResponse(currency, movie);
    }

    private MovieByIdDto getEnrichedMovie(int movieId) {
        return movieEnrichmentService.enrich(movieId, () -> movieMapper.toMovieByIdDto(movieDao.findById(movieId)));
    }

    private MovieByIdDto createResponse(CurrencyType currency, MovieByIdDto movie) {
        movie.setRating(ratingService.getAvgRate(movie.getId()));
        MovieByIdDto movieCopy = movieMapper.copy(movie);

        if (Objects.nonNull(currency)) {
            calculatePrice(currency, movieCopy);
        }
        return movieCopy;
    }

    private void calculatePrice(CurrencyType currency, MovieByIdDto movie) {
        double price = movie.getPrice();
        double excange = exchangeHolder.excange(currency, price);
        movie.setPrice(excange);
    }

    @Override
    @Transactional
    public int save(AddUpdateMovieDto movie) {
        Movie movieToSave = movieMapper.toMovie(movie);
        Movie savedMovie = movieDao.save(movieToSave);

        savedMovie.setCountries(movie.getCountries());
        savedMovie.setGenres(movie.getGenres());

        return savedMovie.getId();
    }

    @Override
    @Transactional
    public void update(int movieId, AddUpdateMovieDto movie) {
        Movie movieForUpdate = movieDao.findByIdForUpdate(movieId);
        movieMapper.updateMovie(movieForUpdate, movie);
        movieForUpdate.setCountries(movie.getCountries());
        movieForUpdate.setGenres(movie.getGenres());
        movieForUpdate.setPoster(movie.getPicturePath());

        CACHED_MOVIES.removeFromCache(movieId);
    }

    @Override
    public void addRating(RatingRequest ratingRequest) {
        ratingService.addRating(ratingRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<MovieFindAllDto> search(String title) {
        List<Integer> foundIds = searchService.search(title);

        return movieDao.findAllById(foundIds).stream()
                .map(movieMapper::toMovieFindAllDto).toList();
    }
}
