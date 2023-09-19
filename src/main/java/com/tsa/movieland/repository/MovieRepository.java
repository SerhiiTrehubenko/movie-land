package com.tsa.movieland.repository;

import com.tsa.movieland.entity.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@com.tsa.movieland.context.JpaRepository
public interface MovieRepository extends JpaRepository<MovieEntity, Integer> {
    List<MovieEntity> findAllByOrderByIdAsc();
    List<MovieEntity> findAllByOrderByPriceAsc();
    List<MovieEntity> findAllByOrderByPriceDesc();
    List<MovieEntity> findAllByOrderByRatingAsc();
    List<MovieEntity> findAllByOrderByRatingDesc();

    @Query(value = "SELECT min(movie_id) minId, max(movie_id) maxId FROM movies", nativeQuery = true)
    Ids findMinMaxId();

    interface Ids {
        Integer getMinId();
        Integer getMaxId();
    }
}


