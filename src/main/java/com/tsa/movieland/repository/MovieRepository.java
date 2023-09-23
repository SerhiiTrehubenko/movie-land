package com.tsa.movieland.repository;

import com.tsa.movieland.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.List;

@com.tsa.movieland.context.JpaRepository
public interface MovieRepository extends JpaRepository<Movie, Integer> {
    @Query(value = "SELECT m FROM Movie m LEFT JOIN FETCH m.posters")
    List<Movie> findAllByOrderByIdAsc();

    @Query(value = "SELECT m FROM Movie m JOIN MovieGenre mg ON m.id = mg.movieId LEFT JOIN FETCH m.posters WHERE mg.genreId = :genreId")
    List<Movie> findByGenreId(int genreId);

    @Override
    @Query(value = "SELECT m FROM Movie m LEFT JOIN FETCH m.posters WHERE m IN :integers")
    @NonNull
    List<Movie> findAllById(@NonNull Iterable<Integer> integers);

    @Query(value = "SELECT min(movie_id) minId, max(movie_id) maxId FROM movies", nativeQuery = true)
    Ids findMinMaxId();

    interface Ids {
        Integer getMinId();
        Integer getMaxId();
    }
}


