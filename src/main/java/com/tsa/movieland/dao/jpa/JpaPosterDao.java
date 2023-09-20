package com.tsa.movieland.dao.jpa;

import com.tsa.movieland.context.JpaDao;
import com.tsa.movieland.dao.PosterDao;
import com.tsa.movieland.dto.PosterDto;
import com.tsa.movieland.entity.Poster;
import com.tsa.movieland.mapper.PosterMapper;
import com.tsa.movieland.repository.PosterRepository;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@JpaDao
@RequiredArgsConstructor
public class JpaPosterDao implements PosterDao {

    private final PosterRepository posterRepository;
    private final PosterMapper posterMapper;

    @Override
    public void add(int movieId, String posterLink) {
        Poster poster = getPoster(movieId, posterLink);
        posterRepository.save(poster);
    }

    private Poster getPoster(int movieId, String posterLink) {
        return Poster.builder()
                .movieId(movieId)
                .link(posterLink)
                .recordTime(new Timestamp(System.currentTimeMillis()))
                .build();
    }

    @Override
    public Iterable<PosterDto> findPosterByMovieId(int movieId) {
        final List<Poster> allByMovieId = posterRepository.findAllByMovieIdOrderByRecordTime(movieId);
        return allByMovieId
                .stream().map(posterMapper::toPosterDto).toList();
    }

    @Override
    public void update(int movieId, String picturePath) {
        String[] pathWithOrderNumber = picturePath.split(";");
        if (pathWithOrderNumber.length != 2) {
            throw new RuntimeException("Bad picturePath: [%s]".formatted(picturePath));
        }

        String path = pathWithOrderNumber[0].trim();
        int orderNumber = Integer.parseInt(pathWithOrderNumber[1].trim());

        List<Poster> allByMovieId = posterRepository.findAllByMovieIdOrderByRecordTime(movieId);
        Poster posterEntity = allByMovieId.get(orderNumber);
        posterRepository.delete(posterEntity);

        posterRepository.save(getPoster(movieId, path));
    }
}
