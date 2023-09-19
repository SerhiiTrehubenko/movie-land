package com.tsa.movieland.dao.jpa;

import com.tsa.movieland.common.AvgRating;
import com.tsa.movieland.common.RatingRequest;
import com.tsa.movieland.context.JpaDao;
import com.tsa.movieland.dao.RatingDao;
import com.tsa.movieland.mapper.RatingMapper;
import com.tsa.movieland.repository.RatingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.List;

@JpaDao
@RequiredArgsConstructor
public class JpaRatingDao implements RatingDao {

    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;
    @Override
    public Iterable<AvgRating> fidAllAvgRatingsGroupedMovie() {
        List<RatingRepository.AverageRating> rating = ratingRepository.getAllAvgRating();
        return rating.stream().map(ratingMapper::toAvgRating).toList();
    }

    @Override
    @Transactional
    public void saveBuffer(Iterator<RatingRequest> iterator) {
        while (iterator.hasNext()) {
            RatingRequest next = iterator.next();
            ratingRepository.saveRatingRequest(next);
            iterator.remove();
        }
    }
}