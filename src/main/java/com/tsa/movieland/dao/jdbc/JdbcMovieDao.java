package com.tsa.movieland.dao.jdbc;

import com.tsa.movieland.dao.MovieDao;
import com.tsa.movieland.dao.jdbc.mapper.*;
import com.tsa.movieland.dto.AddUpdateMovieDto;
import com.tsa.movieland.dto.MovieByIdDto;
import com.tsa.movieland.entity.Movie;
import com.tsa.movieland.entity.MovieFindAllDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.function.Supplier;

@Repository
@RequiredArgsConstructor
public class JdbcMovieDao implements MovieDao {

    private final static String FIND_ALL = "SELECT movie_id, movie_rus_name, movie_native_name, movie_release_year, " +
            "movie_rating, movie_price, movie_posters FROM movies_with_posters";

    private final static String BY_GENRE = "SELECT movie.movie_id, movie_rus_name, movie_native_name, movie_release_year, " +
            "                movie_rating, movie_price, movie_posters " +
            "FROM movies_with_posters movie  " +
            "JOIN movies_genres m_g " +
            "ON movie.movie_id = m_g.movie_id " +
            "WHERE m_g.genre_id = :genreId";

    private final static String GET_MOVIE_ID = "SELECT movie_id FROM movies WHERE movie_rus_name = ? AND movie_native_name = ?";

    @Value("${number.movies.random}")
    private Integer randomNumber;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final MovieFindAllMapper findAllMapper;
    private final MovieByIdMapper descPosterMapper;
    private final MovieMapper movieMapper;

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
                "WHERE movie_id = :movieId";

        return namedParameterJdbcTemplate
                .queryForObject(movieWithDescriptionPoster,
                        Map.of("movieId", movieId),
                        descPosterMapper
                );
    }

    @Override
    public int save(AddUpdateMovieDto movie) {
        String queryInsertMovie = "INSERT INTO movies (movie_rus_name, movie_native_name, movie_release_year, movie_description, movie_price) " +
                "VALUES (:rusName, :nativeName, :releaseYear, :description, :price)";
        Map<String, ? extends Serializable> parametersForInsert = Map.of("rusName", movie.getNameRussian(), "nativeName", movie.getNameNative(), "releaseYear", movie.getYearOfRelease(), "description", movie.getDescription(), "price", movie.getPrice());
        Optional<Integer> movieId = namedParameterJdbcTemplate.execute(queryInsertMovie, parametersForInsert, createCallback(movie.getNameRussian(), movie.getNameNative()));

        Integer movieIdValue = movieId.orElseThrow(() -> new RuntimeException("Movie was not saved"));

        String joinCountry = "INSERT INTO movies_countries (movie_id, country_id) VALUES (:movieId, :joinId);";
        saveJoins(joinCountry, movieIdValue, movie::getCountries);

        String joinGenre = "INSERT INTO movies_genres (movie_id, genre_id) VALUES (:movieId, :joinId);";
        saveJoins(joinGenre, movieIdValue, movie::getGenres);

        return movieIdValue;
    }

    private void saveJoins(String query, Integer movieIdValue, Supplier<List<Integer>> supplier ) {
        List<Integer> joins = supplier.get();
        if (Objects.nonNull(joins) && joins.size() != 0) {
            Map<String, Integer>[] params = createParams(movieIdValue, joins);
            namedParameterJdbcTemplate.batchUpdate(query, params);
        }
    }

    private Map<String, Integer>[] createParams(Integer movieIdValue, List<Integer> joinId) {
        int size = joinId.size();
        List<Map<String, Integer>> mapList = new ArrayList<>(size);
        for (Integer countryId : joinId) {
            mapList.add(Map.of("movieId", movieIdValue, "joinId", countryId));
        }
        @SuppressWarnings("unchecked")
        Map<String, Integer>[] maps = mapList.toArray(new Map[size]);
        return maps;
    }

    private PreparedStatementCallback<Optional<Integer>> createCallback(String nameRussian, String nameNative) {
        return ps -> {
            ps.executeUpdate();
            final Connection connection = ps.getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement(GET_MOVIE_ID);
            try (connection; preparedStatement) {
                preparedStatement.setString(1, nameRussian);
                preparedStatement.setString(2, nameNative);
                final ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return Optional.of(resultSet.getInt("movie_id"));
                }
                return Optional.empty();
            }
        };
    }

    @Override
    public void update(int movieId, AddUpdateMovieDto movie) {
        String queryFindMovieById = "SELECT movie_id, movie_rus_name, movie_native_name, movie_release_year, movie_description, movie_price FROM movies WHERE movie_id = :id;";
        String queryUpdate = "UPDATE movies SET movie_rus_name = :rusName, movie_native_name = :nativeName, movie_release_year = :releaseYear, movie_description = :description, movie_price = :price WHERE movie_id = :id";
        Movie foundMovie = namedParameterJdbcTemplate.query(queryFindMovieById, Map.of("id", movieId), movieMapper);
        if (Objects.isNull(foundMovie)) {
            throw new RuntimeException("Movie with id: [%s] was not found".formatted(movie));
        }
        refresh(foundMovie, movie);
        Map<String, ? extends Serializable> paramUpdate = Map.of("rusName", foundMovie.getNameRussian(), "nativeName", foundMovie.getNameNative(), "releaseYear", foundMovie.getYearOfRelease(), "description", foundMovie.getDescription(), "price", foundMovie.getPrice(), "id", movieId);
        namedParameterJdbcTemplate.update(queryUpdate, paramUpdate);
    }

    private void refresh(Movie foundMovie, AddUpdateMovieDto movie) {
        if (isDifferent(foundMovie.getNameRussian(), movie.getNameRussian())) {
            foundMovie.setNameRussian(movie.getNameRussian());
        }
        if (isDifferent(foundMovie.getNameNative(), movie.getNameNative())) {
            foundMovie.setNameNative(movie.getNameNative());
        }
        if (isDifferent(foundMovie.getYearOfRelease(), movie.getYearOfRelease())) {
            foundMovie.setYearOfRelease(movie.getYearOfRelease());
        }
        if (isDifferent(foundMovie.getDescription(), movie.getDescription())) {
            foundMovie.setDescription(movie.getDescription());
        }
        if (isDifferent(foundMovie.getPrice(), movie.getPrice())) {
            foundMovie.setPrice(movie.getPrice());
        }
    }

    private boolean isDifferent(Object nameRussianDb, Object nameRussianIncome) {
        return !Objects.equals(nameRussianDb, nameRussianIncome) &&
                Objects.nonNull(nameRussianIncome);
    }
}
