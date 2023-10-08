package com.tsa.movieland.dao.jpa.repository;

import com.tsa.movieland.entity.Movie;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {
    @Query(value = "SELECT m FROM Movie m LEFT JOIN FETCH m.posters")
    List<Movie> findAllByOrderByIdAsc();

    @Query(value = "SELECT m FROM Movie m JOIN MovieGenre mg ON m.id = mg.movieId LEFT JOIN FETCH m.posters WHERE mg.genreId = :genreId")
    List<Movie> findByGenreId(int genreId);

    @Query(value = "SELECT m FROM Movie m LEFT JOIN FETCH m.posters ORDER BY FUNCTION('RANDOM')")
    List<Movie> findRandom(Pageable pageable);

    @Override
    @EntityGraph(value = "Movie.byId")
    @NonNull
    Optional<Movie> findById(@NonNull Integer integer);
    @EntityGraph(value = "Movie.byId.Poster")
    Optional<Movie> findByIdOrderById(Integer id);
}


