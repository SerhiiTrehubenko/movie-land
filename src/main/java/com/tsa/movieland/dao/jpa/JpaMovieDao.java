package com.tsa.movieland.dao.jpa;

import com.tsa.movieland.context.JpaDao;
import com.tsa.movieland.dao.MovieDao;
import com.tsa.movieland.dto.AddUpdateMovieDto;
import com.tsa.movieland.dto.MovieByIdDto;
import com.tsa.movieland.entity.MovieCountry;
import com.tsa.movieland.entity.MovieEntity;
import com.tsa.movieland.entity.MovieFindAllDto;
import com.tsa.movieland.entity.MovieGenre;
import com.tsa.movieland.mapper.MovieMapper;
import com.tsa.movieland.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;
import java.util.function.Function;

@JpaDao
@RequiredArgsConstructor
public class JpaMovieDao implements MovieDao {
    private final static Comparator<MovieFindAllDto> COMPARATOR_BY_ID = Comparator.comparing(MovieFindAllDto::getId);
    @Value("${number.movies.random}")
    Integer randomQuantity;
    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final MovieCountryRepository movieCountryRepository;
    private final MovieGenreRepository movieGenreRepository;
    private final MovieMapper movieMapper;

    private final Map<String, Function<MovieRepository, List<MovieEntity>>> methods = Map.of(
            "movie_ratingASC", MovieRepository::findAllByOrderByRatingAsc,
            "movie_ratingDESC", MovieRepository::findAllByOrderByRatingDesc,
            "movie_priceASC", MovieRepository::findAllByOrderByPriceAsc,
            "movie_priceDESC", MovieRepository::findAllByOrderByPriceDesc
    );

    @Override
    public Iterable<MovieFindAllDto> findAll() {
        return movieRepository.findAllByOrderByIdAsc().stream()
                .map(movieMapper::toMovieFindAllDto)
                .toList();
    }

    @Override
    public Iterable<MovieFindAllDto> findAll(String column, String direction) {
        return methods.get(column + direction).apply(movieRepository).stream()
                .map(movieMapper::toMovieFindAllDto)
                .toList();
    }

    @Override
    @Transactional
    public Iterable<MovieFindAllDto> findRandom() {
        MovieRepository.Ids idBounds = movieRepository.findMinMaxId();
        Random random = new Random(47);
        List<Integer> moviesId = random.ints(5, idBounds.getMinId(), idBounds.getMaxId()).distinct().limit(randomQuantity).boxed().toList();
        return movieRepository.findAllById(moviesId).stream()
                .map(movieMapper::toMovieFindAllDto)
                .toList();
    }

    @Override
    public Iterable<MovieFindAllDto> findByGenreId(int genreId) {
        return genreRepository.findById(genreId)
                .orElseThrow()
                .getMovies().stream()
                .map(movieMapper::toMovieFindAllDto)
                .sorted(COMPARATOR_BY_ID)
                .toList();
    }

    @Override
    public Iterable<MovieFindAllDto> findByGenreId(int genreId,
                                                   String column,
                                                   String direction) {
        throw new NotImplementedException();
    }

    @Override
    public MovieByIdDto findById(int movieId) {
        return movieRepository.findById(movieId).map(movieMapper::toMovieByIdDto).orElseThrow();
    }

    @Override
    public int save(AddUpdateMovieDto movie) {
        MovieEntity savedMovie = movieRepository.save(movieMapper.toMovie(movie));

        saveCountries(savedMovie.getId(), movie.getCountries());
        saveGenres(savedMovie.getId(), movie.getGenres());

        return savedMovie.getId();
    }

    private void saveCountries(int movieId, List<Integer> countriesId) {
        List<MovieCountry> countries = countriesId.stream().map(id -> MovieCountry.builder()
                        .movieId(movieId)
                        .countryId(id)
                        .build()).toList();
        movieCountryRepository.saveAll(countries);
    }

    private void saveGenres(int movieId, List<Integer> countriesId) {
        List<MovieGenre> genres = countriesId.stream().map(id -> MovieGenre.builder()
                        .movieId(movieId)
                        .genreId(id)
                        .build())
                .toList();
        movieGenreRepository.saveAll(genres);
    }

    @Override
    @Transactional
    public void update(int movieId, AddUpdateMovieDto movie) {
        MovieEntity movieEntity = movieRepository.findById(movieId).orElseThrow();
        movieMapper.updateMovie(movieEntity, movie);

        if (Objects.nonNull(movie.getCountries()) && movie.getCountries().size() != 0) {
            movieCountryRepository.deleteAllByMovieId(movieId);
            saveCountries(movieId, movie.getCountries());
        }

        if (Objects.nonNull(movie.getGenres()) && movie.getGenres().size() != 0) {
            movieGenreRepository.deleteAllByMovieId(movieId);
            saveGenres(movieId, movie.getGenres());
        }
    }
}
