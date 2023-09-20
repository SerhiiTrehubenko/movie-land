package com.tsa.movieland.dao.jdbc;

import com.tsa.movieland.context.JdbcDao;
import com.tsa.movieland.dao.PosterDao;
import com.tsa.movieland.dao.jdbc.mapper.PosterMapper;
import com.tsa.movieland.dao.jdbc.mapper.PosterUpdateMapper;
import com.tsa.movieland.dto.PosterDto;
import com.tsa.movieland.dto.PosterUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@JdbcDao
public class JdbcPosterDao implements PosterDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final PosterMapper posterMapper;
    private final PosterUpdateMapper posterUpdateMapper;
    @Override
    public void add(int movieId, String posterLink) {
        String addPoster = "INSERT INTO posters (movie_id, poster_link) VALUES (:id, :link)";
        namedParameterJdbcTemplate.update(addPoster, Map.of("id", movieId, "link", posterLink));
    }

    @Override
    public Iterable<PosterDto> findPosterByMovieId(int movieId) {
        String findPosters = "SELECT movie_id, poster_link FROM posters where movie_id = :id ORDER BY poster_record_time";
        return namedParameterJdbcTemplate.query(findPosters, Map.of("id", movieId), posterMapper);
    }

    @Override
    public void update(int movieId, String picturePath) {
        String[] pathWithOrderNumber = picturePath.split(";");
        if (pathWithOrderNumber.length != 2) {
            throw new RuntimeException("Bad picturePath: [%s]".formatted(picturePath));
        }

        String path = pathWithOrderNumber[0].trim();
        int orderNumber = Integer.parseInt(pathWithOrderNumber[1].trim());

        String findPosters = "SELECT movie_id, poster_link, poster_record_time FROM posters where movie_id = :id ORDER BY poster_record_time";
        List<PosterUpdate> foundPosters = namedParameterJdbcTemplate.query(findPosters, Map.of("id", movieId), posterUpdateMapper);

        PosterUpdate posterUpdate = foundPosters.get(orderNumber);
        posterUpdate.setLink(path);

        String updateQuery = "UPDATE posters SET poster_link = :link WHERE movie_id = :id AND poster_record_time = :time";
        namedParameterJdbcTemplate.update(updateQuery, Map.of("link", posterUpdate.getLink() , "id", movieId, "time", posterUpdate.getRecordTime()));
    }
}
