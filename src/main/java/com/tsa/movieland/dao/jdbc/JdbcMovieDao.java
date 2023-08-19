package com.tsa.movieland.dao.jdbc;

import com.tsa.movieland.dao.MovieDao;
import com.tsa.movieland.dao.jdbc.callback.AddMovieCallback;
import com.tsa.movieland.dao.jdbc.mapper.*;
import com.tsa.movieland.dto.AddUpdateMovieDto;
import com.tsa.movieland.dto.MovieByIdDto;
import com.tsa.movieland.entity.Movie;
import com.tsa.movieland.entity.MovieFindAllDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.Supplier;

@Repository
@RequiredArgsConstructor
public class JdbcMovieDao implements MovieDao {
    private final static String ID = "id";
    private final static String FIND_ALL = "SELECT movie_id, movie_rus_name, movie_native_name, movie_release_year, " +
            "movie_rating, movie_price, movie_posters FROM movies_with_posters";

    private final static String BY_GENRE = "SELECT movie.movie_id, movie_rus_name, movie_native_name, movie_release_year, " +
            "                movie_rating, movie_price, movie_posters " +
            "FROM movies_with_posters movie  " +
            "JOIN movies_genres m_g " +
            "ON movie.movie_id = m_g.movie_id " +
            "WHERE m_g.genre_id = :genreId";

    @Value("${number.movies.random}")
    private Integer randomNumber;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final MovieFindAllMapper findAllMapper;
    private final MovieByIdMapper descPosterMapper;
    private final MovieMapper movieMapper;
    private final AddMovieCallback addMovieCallback;

    @Override
    public Iterable<MovieFindAllDto> findAll() {
        return namedParameterJdbcTemplate.query(FIND_ALL, findAllMapper);
    }

    @Override
    public Iterable<MovieFindAllDto> findRandom() {
        String query = FIND_ALL + " ORDER BY random() LIMIT :randomNumber;";
        var parameters = new MapSqlParameterSource("randomNumber", randomNumber);
        return namedParameterJdbcTemplate.query(query, parameters, findAllMapper);
    }

    @Override
    public Iterable<MovieFindAllDto> findByGenreId(int genreId) {
        var parameters = new MapSqlParameterSource("genreId", genreId);
        return namedParameterJdbcTemplate.query(BY_GENRE, parameters, findAllMapper);
    }

    @Override
    public Iterable<MovieFindAllDto> findAll(String column, String direction) {
        String query = FIND_ALL + " ORDER BY %s %s;".formatted(column, direction);

        return namedParameterJdbcTemplate.query(query, findAllMapper);
    }

    @Override
    public Iterable<MovieFindAllDto> findByGenreId(int genreId, String column, String direction) {
        String query = BY_GENRE + " ORDER BY %s %s;".formatted(column, direction);
        var parameters = new MapSqlParameterSource("genreId", genreId);
        return namedParameterJdbcTemplate.query(query, parameters, findAllMapper);
    }

    @Override
    public MovieByIdDto findById(int movieId) {
        String movieWithDescriptionPoster = "SELECT movie_id, movie_rus_name, movie_native_name, " +
                "movie_release_year, movie_description, movie_rating, movie_price, movie_posters FROM movies_with_description_posters " +
                "WHERE movie_id = :id";

        return namedParameterJdbcTemplate
                .queryForObject(movieWithDescriptionPoster,
                        Map.of(ID, movieId),
                        descPosterMapper
                );
    }

    @Override
    public int save(AddUpdateMovieDto movie) {
        String queryInsertMovie = "INSERT INTO movies (movie_rus_name, movie_native_name, movie_release_year, movie_description, movie_price) " +
                "VALUES (:rusName, :nativeName, :releaseYear, :description, :price)";
        Map<String, Object> parametersForInsert = createInsertParams(movie);
        Optional<Integer> movieId =
                namedParameterJdbcTemplate
                        .execute(queryInsertMovie,
                                parametersForInsert,
                                addMovieCallback.getInstance(movie.getNameRussian(), movie.getNameNative()));

        Integer movieIdValue = movieId.orElseThrow(() -> new RuntimeException("Movie was not saved"));

        saveCountryJoins(movie, movieIdValue);

        saveGenresJoins(movie, movieIdValue);

        return movieIdValue;
    }

    private Map<String, Object> createInsertParams(AddUpdateMovieDto movie) {
        return Map.of(
                "rusName", movie.getNameRussian(),
                "nativeName", movie.getNameNative(),
                "releaseYear", movie.getYearOfRelease(),
                "description", movie.getDescription(),
                "price", movie.getPrice()
        );
    }

    private void saveCountryJoins(AddUpdateMovieDto movie, Integer movieIdValue) {
        String joinCountry = "INSERT INTO movies_countries (movie_id, country_id) VALUES (:id, :joinId);";
        saveJoins(joinCountry, movieIdValue, movie::getCountries);
    }

    private void saveGenresJoins(AddUpdateMovieDto movie, Integer movieIdValue) {
        String joinGenre = "INSERT INTO movies_genres (movie_id, genre_id) VALUES (:id, :joinId);";
        saveJoins(joinGenre, movieIdValue, movie::getGenres);
    }

    private void saveJoins(String query, Integer movieIdValue, Supplier<List<Integer>> supplier) {
        List<Integer> joins = supplier.get();
        if (Objects.nonNull(joins) && joins.size() != 0) {
            Map<String, Integer>[] params = createJoinParams(movieIdValue, joins);
            namedParameterJdbcTemplate.batchUpdate(query, params);
        }
    }

    private Map<String, Integer>[] createJoinParams(Integer movieIdValue, List<Integer> joinId) {
        int size = joinId.size();
        List<Map<String, Integer>> mapList = new ArrayList<>(size);
        for (Integer countryId : joinId) {
            mapList.add(Map.of(ID, movieIdValue, "joinId", countryId));
        }
        @SuppressWarnings("unchecked")
        Map<String, Integer>[] maps = mapList.toArray(new Map[size]);
        return maps;
    }

    @Override
    public void update(int movieId, AddUpdateMovieDto movie) {
        String queryFindMovieById = "SELECT movie_id, movie_rus_name, movie_native_name, movie_release_year, movie_description, movie_price FROM movies WHERE movie_id = :id;";
        String queryUpdate = "UPDATE movies SET movie_rus_name = :rusName, movie_native_name = :nativeName, movie_release_year = :releaseYear, movie_description = :description, movie_price = :price WHERE movie_id = :id";
        Movie foundMovie = namedParameterJdbcTemplate.query(queryFindMovieById, Map.of(ID, movieId), movieMapper);
        if (Objects.isNull(foundMovie)) {
            throw new RuntimeException("Movie with id: [%s] was not found".formatted(movie));
        }
        refresh(foundMovie, movie);
        Map<String, Object> paramUpdate = createUpdateParams(movieId, movie);
        namedParameterJdbcTemplate.update(queryUpdate, paramUpdate);

        updateCountries(movieId, movie);

        updateGenres(movieId, movie);
    }

    private Map<String, Object> createUpdateParams(Integer movieId, AddUpdateMovieDto movieDto) {
        final Map<String, Object> updateParams = new HashMap<>(createInsertParams(movieDto));
        updateParams.put(ID, movieId);
        return updateParams;
    }

    private void updateCountries(int movieId, AddUpdateMovieDto movie) {
        List<Integer> countries = movie.getCountries();
        if (Objects.nonNull(countries) && countries.size() != 0) {
            String deleteOldCountries = "DELETE FROM movies_countries WHERE movie_id = :id;";
            namedParameterJdbcTemplate.update(deleteOldCountries, Map.of(ID, movieId));
            saveCountryJoins(movie, movieId);
        }
    }

    private void updateGenres(int movieId, AddUpdateMovieDto movie) {
        List<Integer> genres = movie.getGenres();
        if (Objects.nonNull(genres) && genres.size() != 0) {
            String deleteOldCountries = "DELETE FROM movies_genres WHERE movie_id = :id;";
            namedParameterJdbcTemplate.update(deleteOldCountries, Map.of(ID, movieId));
            saveGenresJoins(movie, movieId);
        }
    }

    private void refresh(Movie foundMovie, AddUpdateMovieDto movie) {
        if (Objects.isNull(movie.getNameRussian())) {
            movie.setNameRussian(foundMovie.getNameRussian());
        }
        if (Objects.isNull(movie.getNameNative())) {
            movie.setNameNative(foundMovie.getNameNative());
        }
        if (Objects.isNull(movie.getYearOfRelease())) {
            movie.setYearOfRelease(foundMovie.getYearOfRelease());
        }
        if (Objects.isNull(movie.getDescription())) {
            movie.setDescription(foundMovie.getDescription());
        }
        if (Objects.isNull(movie.getPrice())) {
            movie.setPrice(foundMovie.getPrice());
        }
    }
}
