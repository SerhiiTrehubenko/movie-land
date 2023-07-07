package com.tsa.movieland.service;

import com.tsa.movieland.dao.MovieDao;
import com.tsa.movieland.entity.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DefaultMovieService implements MovieService {
    private final static Comparator<Movie> COMPARATOR_BY_RATING = Comparator.comparing(Movie::rating);
    private final static Comparator<Movie> COMPARATOR_BY_PRICE_ASC = Comparator.comparing(Movie::price);

    private final MovieDao movieDao;

    @Override
    public List<Movie> findAll() {
        return movieDao.findAll();
    }

    @Override
    public List<Movie> findAll(String rating, String price) {
        if (Objects.nonNull(rating)) {
            return findAllSortByRating(rating);
        }
        if (Objects.nonNull(price)) {
            return findAllSortByPrice(price);
        }
        return movieDao.findAll();
    }

    private List<Movie> findAllSortByRating(String order) {
        List<Movie> movies = movieDao.findAll();
        sortByRating(movies, order);

        return movies;
    }

    private void sortByRating(List<Movie> movies, String order) {
        if (order.equalsIgnoreCase("asc")) {
            movies.sort(COMPARATOR_BY_RATING);
        } else {
            movies.sort(COMPARATOR_BY_RATING.reversed());
        }
    }

    private List<Movie> findAllSortByPrice(String order) {
        List<Movie> movies = movieDao.findAll();
        sortByPrice(movies, order);

        return movies;
    }

    private void sortByPrice(List<Movie> movies, String order) {
        if (order.equalsIgnoreCase("asc")) {
            movies.sort(COMPARATOR_BY_PRICE_ASC);
        } else {
            movies.sort(COMPARATOR_BY_PRICE_ASC.reversed());
        }
    }

    @Override
    public List<Movie> findThreeRandom() {
        return movieDao.findThreeRandom();
    }

    @Override
    public List<Movie> findByGenre(int genreId, String rating, String price) {
        if (Objects.nonNull(rating)) {
            return findByGenreSortByRating(genreId, rating);
        }
        if (Objects.nonNull(price)) {
            return findByGenreSortByPrice(genreId, price);
        }
        return movieDao.findByGenreId(genreId);
    }

    private List<Movie> findByGenreSortByRating(int genreId, String order) {
        List<Movie> movies = movieDao.findByGenreId(genreId);
        sortByRating(movies, order);

        return movies;
    }

    private List<Movie> findByGenreSortByPrice(int genreId, String order) {
        List<Movie> movies = movieDao.findByGenreId(genreId);
        sortByPrice(movies, order);

        return movies;
    }
}
