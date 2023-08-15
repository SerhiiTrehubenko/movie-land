package com.tsa.movieland.dao.jdbc;

import com.tsa.movieland.dao.PosterDao;
import com.tsa.movieland.dao.jdbc.mapper.PosterMapper;
import com.tsa.movieland.entity.Poster;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@RequiredArgsConstructor
public class JdbcPosterDao implements PosterDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final PosterMapper posterMapper;
    @Override
    public void addPoster(int movieId, String posterLink) {
        String addPoster = "INSERT INTO posters (movie_id, poster_link) VALUES (:id, :link)";
        namedParameterJdbcTemplate.update(addPoster, Map.of("id", movieId, "link", posterLink));
    }

    @Override
    public Iterable<Poster> findPosterByMovieId(int movieId) {
        String findPosters = "SELECT movie_id, poster_link FROM posters where movie_id = :id";
        return namedParameterJdbcTemplate.query(findPosters, Map.of("id", movieId), posterMapper);
    }
}
