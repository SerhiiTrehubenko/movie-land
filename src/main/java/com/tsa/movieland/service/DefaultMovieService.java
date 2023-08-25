package com.tsa.movieland.service;

import com.tsa.movieland.currency.CurrencyExchangeHolder;
import com.tsa.movieland.currency.CurrencyType;
import com.tsa.movieland.dao.MovieDao;
import com.tsa.movieland.common.*;
import com.tsa.movieland.dto.AddUpdateMovieDto;
import com.tsa.movieland.dto.MovieByIdDto;
import com.tsa.movieland.entity.MovieFindAllDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class DefaultMovieService implements MovieService {
    private final static ExecutorService THREAD_POOL = Executors.newSingleThreadExecutor();
    private final static Map<Integer, SoftReference<MovieByIdDto>> CACHED_MOVIES = new ConcurrentHashMap<>();

    private final MovieDao movieDao;
    private final PosterService posterService;
    private final MovieEnrichmentService movieEnrichmentService;
    private final CurrencyExchangeHolder exchangeHolder;

    @Override
    public Iterable<MovieFindAllDto> findAll(MovieRequest movieRequest) {
        if (notEmptyMovieRequest(movieRequest)) {
            return movieDao.findAll(field(movieRequest), direction(movieRequest));
        }
        return movieDao.findAll();
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
        return movieDao.findRandom();
    }

    @Override
    public Iterable<MovieFindAllDto> findByGenre(int genreId, MovieRequest movieRequest) {
        if (notEmptyMovieRequest(movieRequest)) {
            return movieDao.findByGenreId(genreId, field(movieRequest), direction(movieRequest));
        }
        return movieDao.findByGenreId(genreId);
    }

    @Override
    public MovieByIdDto getById(int movieId, MovieRequest movieRequest) {
        if (isCached(movieId)) {
            return movieFromCache(movieId, movieRequest);
        }
        return movieFromDb(movieId, movieRequest);
    }

    private boolean isCached(int movieId) {
        if (CACHED_MOVIES.containsKey(movieId)) {
            SoftReference<MovieByIdDto> movieReference = CACHED_MOVIES.get(movieId);
            MovieByIdDto movie = movieReference.get();
            return Objects.nonNull(movie);
        }
        return false;
    }

    private MovieByIdDto movieFromCache(int movieId, MovieRequest movieRequest) {
        CurrencyType currency = movieRequest.getCurrencyType();
        MovieByIdDto movie = getFromCache(movieId);

        return createResponse(currency, movie);
    }

    private MovieByIdDto getFromCache(int movieId) {
        SoftReference<MovieByIdDto> movieReference = CACHED_MOVIES.get(movieId);
        final MovieByIdDto movie = movieReference.get();
        Objects.requireNonNull(movie);
        return movie;
    }

    private MovieByIdDto createResponse(CurrencyType currency, MovieByIdDto movie) {
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

    private MovieByIdDto movieFromDb(int movieId, MovieRequest movieRequest) {
        CurrencyType currency = movieRequest.getCurrencyType();

        MovieByIdDto movieByIdDto = getEnrichedMovie(movieId);

        addToCache(movieId, movieByIdDto);

        return createResponse(currency, movieByIdDto);
    }

    private MovieByIdDto getEnrichedMovie(int movieId) {
        Future<MovieByIdDto> taskMovie = THREAD_POOL.submit(() -> movieDao.findById(movieId));

        MovieByIdDto movie = getResult(taskMovie);

        return movieEnrichmentService.enrich(movie);
    }

    @SneakyThrows
    private <V> V getResult(Future<V> task) {
        return task.get(5, TimeUnit.SECONDS);
    }

    private void addToCache(int movieId, MovieByIdDto movieByIdDto) {
        CACHED_MOVIES.put(movieId, new SoftReference<>(movieByIdDto));
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
        if (isCached(movieId)) {
            removeFromCache(movieId);
        }
        movieDao.update(movieId, movie);
        posterService.update(movieId, movie.getPicturePath());
    }

    private void removeFromCache(int movieId) {
        CACHED_MOVIES.remove(movieId);
    }
}
