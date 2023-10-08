package com.tsa.movieland.service;

import com.tsa.movieland.common.RatingRequest;
import com.tsa.movieland.dao.RatingDao;
import com.tsa.movieland.common.AvgRating;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultRatingService implements RatingService {

    private final RatingDao ratingDao;
    private volatile Map<Integer, AvgRating> avgRatingsCache;
    private final static Set<RatingRequest> ratingsAggregatorBuffer = Collections.synchronizedSet(new HashSet<>());
    @Override
    public double getAvgRate(int movieID) {
        if (avgRatingsCache.containsKey(movieID)) {
            return avgRatingsCache.get(movieID).getCurrentAvg();
        }
        return 0.0;
    }

    @Override
    public void addRating(RatingRequest ratingRequest) {
        ratingsAggregatorBuffer.add(ratingRequest);
        updateOrAddRatingInCache(ratingRequest);
    }

    private void updateOrAddRatingInCache(RatingRequest ratingRequest) {
        int movieId = ratingRequest.getMovieId();
        if (avgRatingsCache.containsKey(movieId)) {
            AvgRating avgRatingDto = avgRatingsCache.get(movieId);
            avgRatingDto.updateAvgRating(ratingRequest.getRating());
        } else {
            addNewAvgRatingToCache(ratingRequest);
        }
    }

    private void addNewAvgRatingToCache(RatingRequest ratingRequest) {
        int movieId = ratingRequest.getMovieId();

        AvgRating newRating = AvgRating.builder()
                .movieId(movieId)
                .currentAvg(ratingRequest.getRating())
                .userVotes(1L)
                .build();

        avgRatingsCache.put(movieId, newRating);
    }

    @Scheduled(cron = "${ratings.flush-buffer}")
    private void flushToDb() {
        if (!ratingsAggregatorBuffer.isEmpty()) {
            ratingDao.saveBuffer(ratingsAggregatorBuffer);
            log.info("Rating buffer has been flushed");
        }
    }

    @PostConstruct
    @Scheduled(cron = "${ratings.refresh-avg-rating}")
    private void fillAvgRatingsCache() {

        List<AvgRating> ratingDtos = (List<AvgRating>) ratingDao.fidAllAvgRatingsGroupedMovie();
        Objects.requireNonNull(ratingDtos);
        log.info("Average Rating Cache was filled");
        if (Objects.isNull(avgRatingsCache)) {
            avgRatingsCache = ratingDtos.stream().collect(Collectors.toConcurrentMap(AvgRating::getMovieId, avgRatingDto -> avgRatingDto));
        } else {
            Map<Integer, AvgRating> refreshedAvgRatings = ratingDtos.stream().collect(Collectors.toConcurrentMap(AvgRating::getMovieId, avgRatingDto -> avgRatingDto));
            avgRatingsCache.putAll(refreshedAvgRatings);
        }
    }
}
