package com.tsa.movieland.domain;

import com.tsa.movieland.dao.MovieDao;
import com.tsa.movieland.entity.Movie;
import lombok.Builder;

import java.util.function.BiFunction;
import java.util.function.Function;

@Builder
public class SortDirection {
    private static final String PRICE = "price";
    private static final String RATING = "rating";
    private static final String ASC = "asc";
    private static final String DESC = "desc";

    private final BiFunction<MovieDao, String, Iterable<Movie>> byPrice = MovieDao::findAllSortPrice;
    private final BiFunction<MovieDao, String, Iterable<Movie>> byRating = MovieDao::findAllSortRating;
    private final TrioFunction<MovieDao, Integer, String, Iterable<Movie>> genreByPrice = MovieDao::findByGenreIdSortPrice;
    private final TrioFunction<MovieDao, Integer, String, Iterable<Movie>> genreByRating = MovieDao::findByGenreIdSortRating;
    private final Function<MovieDao, Iterable<Movie>> findAll = MovieDao::findAll;
    private final BiFunction<MovieDao, Integer, Iterable<Movie>> findByGenreId = MovieDao::findByGenreId;

    private final String typeOfSorting;
    private final String direction;

    public Iterable<Movie> findAll(MovieDao movieDao) {
        if (typeOfSorting.equalsIgnoreCase(PRICE)) {
            return sort(byPrice, movieDao);
        }

        if (typeOfSorting.equalsIgnoreCase(RATING)) {
            return sort(byRating, movieDao);
        }
        return findAll.apply(movieDao);
    }

    private Iterable<Movie> sort(BiFunction<MovieDao, String, Iterable<Movie>> sortMethod,
                                 MovieDao movieDao) {
        if (direction.equalsIgnoreCase(ASC)) {
            return sortMethod.apply(movieDao, ASC);
        } else {
            return sortMethod.apply(movieDao, DESC);
        }
    }

    public Iterable<Movie> findByGenreId(int genreId, MovieDao movieDao) {
        if (typeOfSorting.equalsIgnoreCase(PRICE)) {
            return sortByGenre(genreByPrice, genreId, movieDao);
        }

        if (typeOfSorting.equalsIgnoreCase(RATING)) {
            return sortByGenre(genreByRating, genreId, movieDao);
        }
        return findByGenreId.apply(movieDao, genreId);
    }

    private Iterable<Movie> sortByGenre(TrioFunction<MovieDao, Integer, String, Iterable<Movie>> sortMethod,
                                 int genreId,
                                 MovieDao movieDao) {
        if (direction.equalsIgnoreCase(ASC)) {
            return sortMethod.apply(movieDao, genreId, ASC);
        } else {
            return sortMethod.apply(movieDao, genreId, DESC);
        }
    }
}
