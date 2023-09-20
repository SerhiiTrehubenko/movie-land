package com.tsa.movieland.dao.jpa;

import com.tsa.movieland.context.JpaDao;
import com.tsa.movieland.dao.MovieDao;
import com.tsa.movieland.dto.AddUpdateMovieDto;
import com.tsa.movieland.dto.MovieByIdDto;
import com.tsa.movieland.entity.MovieCountry;
import com.tsa.movieland.entity.Movie;
import com.tsa.movieland.dto.MovieFindAllDto;
import com.tsa.movieland.entity.MovieGenre;
import com.tsa.movieland.exception.GenreNotFoundException;
import com.tsa.movieland.exception.MovieNotFoundException;
import com.tsa.movieland.mapper.MovieMapper;
import com.tsa.movieland.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@JpaDao
@RequiredArgsConstructor
public class JpaMovieDao implements MovieDao {
    private final static Comparator<MovieFindAllDto> COMPARATOR_BY_ID = Comparator.comparing(MovieFindAllDto::getId);

    private final Map<Boolean, BiConsumer<Integer, AddUpdateMovieDto>> updateCountry = Map.of(
            Boolean.TRUE, this::updateCountries,
            Boolean.FALSE, (movieId, movie) -> {
                // do nothing
            });
    private final Map<Boolean, BiConsumer<Integer, AddUpdateMovieDto>> updateGenres = Map.of(
            Boolean.TRUE, this::updateGenres,
            Boolean.FALSE, (movieId, movie) -> {
                // do nothing
            });
    @Value("${number.movies.random}")
    Integer randomQuantity;
    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final MovieCountryRepository movieCountryRepository;
    private final MovieGenreRepository movieGenreRepository;
    private final MovieMapper movieMapper;

    @Override
    public Iterable<MovieFindAllDto> findAll() {
        return movieRepository.findAllByOrderByIdAsc().stream()
                .map(movieMapper::toMovieFindAllDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public Iterable<MovieFindAllDto> findAll(String column, String direction) {
        throw new NotImplementedException();
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
                .orElseThrow(() -> new GenreNotFoundException("Genre with id: [%d] is absent".formatted(genreId)))
                .getMovies().stream()
                .map(movieMapper::toMovieFindAllDto)
                .sorted(COMPARATOR_BY_ID)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public Iterable<MovieFindAllDto> findByGenreId(int genreId,
                                                   String column,
                                                   String direction) {
        throw new NotImplementedException();
    }

    @Override
    public MovieByIdDto findById(int movieId) {
        return movieRepository.findById(movieId)
                .map(movieMapper::toMovieByIdDto)
                .orElseThrow(() -> this.throwMovieNotFoundException(movieId));
    }

    private MovieNotFoundException throwMovieNotFoundException(int movieId) {
        return new MovieNotFoundException("MovieDto with id: [%d] is absent".formatted(movieId));
    }

    @Override
    public int save(AddUpdateMovieDto movie) {
        Movie savedMovie = movieRepository.save(movieMapper.toMovie(movie));

        saveCountries(savedMovie.getId(), movie.getCountries());
        saveGenres(savedMovie.getId(), movie.getGenres());

        return savedMovie.getId();
    }

    private void saveCountries(int movieId, List<Integer> countriesId) {
        List<MovieCountry> countries = countriesId.stream()
                .map(
                        id -> MovieCountry.builder()
                                .movieId(movieId)
                                .countryId(id)
                                .build()
                ).toList();
        movieCountryRepository.saveAll(countries);
    }

    private void saveGenres(int movieId, List<Integer> countriesId) {
        List<MovieGenre> genres = countriesId.stream()
                .map(
                        id -> MovieGenre.builder()
                        .movieId(movieId)
                        .genreId(id)
                        .build()
                ).toList();
        movieGenreRepository.saveAll(genres);
    }

    @Override
    @Transactional
    public void update(int movieId, AddUpdateMovieDto movie) {
        Movie movieToUpdate = movieRepository.findById(movieId)
                .orElseThrow(() -> this.throwMovieNotFoundException(movieId));
        movieMapper.updateMovie(movieToUpdate, movie);

        updateCountry.get(Objects.nonNull(movie.getCountries()) &&
                        movie.getCountries().size() != 0)
                .accept(movieId, movie);

        updateGenres.get(Objects.nonNull(movie.getGenres()) &&
                        movie.getGenres().size() != 0)
                .accept(movieId, movie);
    }

    private void updateCountries(Integer movieId, AddUpdateMovieDto movie) {
        movieCountryRepository.deleteAllByMovieId(movieId);
        saveCountries(movieId, movie.getCountries());
    }

    private void updateGenres(Integer movieId, AddUpdateMovieDto movie) {
        movieGenreRepository.deleteAllByMovieId(movieId);
        saveGenres(movieId, movie.getGenres());
    }
}
