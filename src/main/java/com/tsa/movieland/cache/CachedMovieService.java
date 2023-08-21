package com.tsa.movieland.cache;

import com.tsa.movieland.common.MovieRequest;
import com.tsa.movieland.context.Cache;
import com.tsa.movieland.currency.CurrencyExchangeHolder;
import com.tsa.movieland.currency.CurrencyType;
import com.tsa.movieland.dto.AddUpdateMovieDto;
import com.tsa.movieland.dto.MovieByIdDto;
import com.tsa.movieland.entity.Country;
import com.tsa.movieland.entity.Genre;
import com.tsa.movieland.entity.MovieFindAllDto;
import com.tsa.movieland.entity.Review;
import com.tsa.movieland.copier.MovieByIdCopier;
import com.tsa.movieland.service.*;
import com.tsa.movieland.service.parallel.ResultExtractor;
import com.tsa.movieland.service.parallel.ThreadExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;

import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Cache
@Primary
@RequiredArgsConstructor
@Slf4j
public class CachedMovieService implements MovieService {
    private volatile SoftReference<Map<Integer, MovieByIdDto>> cachedMovies = new SoftReference<>(new ConcurrentHashMap<>());
    private final MovieService movieService;
    private final CurrencyExchangeHolder exchangeHolder;
    private final CountryService countryService;
    private final GenreService genreService;
    private final ReviewService reviewService;
    private final ThreadExecutor executor;
    private final MovieByIdCopier movieCopier;

    @Override
    public Iterable<MovieFindAllDto> findRandom() {
        return movieService.findRandom();
    }

    @Override
    public Iterable<MovieFindAllDto> findAll(MovieRequest movieRequest) {
        return movieService.findAll(movieRequest);
    }

    @Override
    public Iterable<MovieFindAllDto> findByGenre(int genreId, MovieRequest movieRequest) {
        return movieService.findByGenre(genreId, movieRequest);
    }

    @Override
    public MovieByIdDto getById(int movieId, MovieRequest movieRequest) {
        if (isCached(movieId)) {
            return movieFromCache(movieId, movieRequest);
        }
        return movieFromDb(movieId, movieRequest);
    }

    private boolean isCached(int movieId) {
        Map<Integer, MovieByIdDto> movies = cachedMovies.get();
        return Objects.nonNull(movies) && movies.containsKey(movieId);
    }

    private MovieByIdDto movieFromCache(int movieId, MovieRequest movieRequest) {
        CurrencyType currency = movieRequest.getCurrencyType();
        MovieByIdDto movie = getFromCache(movieId);

        return createResponse(currency, movie);
    }

    private MovieByIdDto getFromCache(int movieId) {
        Map<Integer, MovieByIdDto> movies = cachedMovies.get();
        Objects.requireNonNull(movies);
        return movies.get(movieId);
    }

    private MovieByIdDto createResponse(CurrencyType currency, MovieByIdDto movie) {
        MovieByIdDto movieCopy = movieCopier.copy(movie);
        calculatePrice(currency, movieCopy);

        return movieCopy;
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
        processRequest(movieId, movieRequest);
        MovieByIdDto movieByIdDto = getMovieByIdDto();

        addToCache(movieId, movieByIdDto);

        return createResponse(currency, movieByIdDto);
    }

    private void processRequest(int movieId, MovieRequest movieRequest) {
        executor.addMethodTwoParam(movieService::getById, movieId, movieRequest);
        executor.addMethodOneParam(countryService::findByMovieId, movieId);
        executor.addMethodOneParam(genreService::findByMovieId, movieId);
        executor.addMethodOneParam(reviewService::findByMovieId, movieId);
    }

    private MovieByIdDto getMovieByIdDto() {
        ResultExtractor resultExtractor = executor.executeTasks();
        MovieByIdDto result = resultExtractor.getObject(MovieByIdDto.class);
        result.setCountries(resultExtractor.getList(Country.class));
        result.setGenres(resultExtractor.getList(Genre.class));
        result.setReviews(resultExtractor.getList(Review.class));
        return result;
    }

    private void addToCache(int movieId, MovieByIdDto movieByIdDto) {
        Map<Integer, MovieByIdDto> moviesInCache = cachedMovies.get();
        if (Objects.isNull(moviesInCache)) {
            ConcurrentHashMap<Integer, MovieByIdDto> map = new ConcurrentHashMap<>();
            map.put(movieId, movieByIdDto);
            cachedMovies = new SoftReference<>(map);
            log.info("Movie Cache has been reloaded");
            return;
        }
        moviesInCache.put(movieId, movieByIdDto);
    }

    @Override
    public int save(AddUpdateMovieDto movie) {
        return movieService.save(movie);
    }

    @Override
    public void update(int movieId, AddUpdateMovieDto movie) {
        if (isCached(movieId)) {
            removeFromCache(movieId);
        }
        movieService.update(movieId, movie);
    }

    private void removeFromCache(int movieId) {
        Map<Integer, MovieByIdDto> movies = cachedMovies.get();
        if (Objects.nonNull(movies)) {
            movies.remove(movieId);
        }
    }
}
