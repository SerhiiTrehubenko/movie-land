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
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultRatingService implements RatingService {

    private final RatingDao ratingDao;
    private volatile Map<Integer, AvgRating> avgRatingsCache;
    private Map<String, List<RatingRequest>> ratingsAggregatorBuffer = new ConcurrentHashMap<>();

    @Override
    public double getAvgRate(int movieID) {
        if (avgRatingsCache.containsKey(movieID)) {
            return avgRatingsCache.get(movieID).getCurrentAvg();
        }
        return 0.0;
    }

    @Override
    public void addRating(RatingRequest ratingRequest) {
        String userEmail = ratingRequest.getUserEmail();
        ratingsAggregatorBuffer.computeIfAbsent(userEmail, rating -> new ArrayList<>()).add(ratingRequest);

        updateRatingInCache(ratingRequest);
    }

    private void updateRatingInCache(RatingRequest ratingRequest) {
        String userEmail = ratingRequest.getUserEmail();
        int movieId = ratingRequest.getMovieId();
        if (avgRatingsCache.containsKey(movieId)) {
            AvgRating avgRatingDto = avgRatingsCache.get(movieId);
            avgRatingDto.updateAvgRating(userEmail, ratingRequest.getRating());
        } else {
            addNewAvgRatingToCache(ratingRequest);
        }
    }

    private void addNewAvgRatingToCache(RatingRequest ratingRequest) {
        int movieId = ratingRequest.getMovieId();
        Map<String, Double> userRatings = new HashMap<>();
        userRatings.put(ratingRequest.getUserEmail(), ratingRequest.getRating());

        AvgRating newRating = AvgRating.builder()
                .movieId(movieId)
                .currentAvg(ratingRequest.getRating())
                .userVotes(userRatings).build();

        avgRatingsCache.put(movieId, newRating);
    }

    @Scheduled(cron = "${ratings.flush-buffer}")
    private void flushToDb() {
        if (!ratingsAggregatorBuffer.isEmpty()) {
            ratingDao.saveBuffer(ratingsAggregatorBuffer.entrySet());
            ratingsAggregatorBuffer = new ConcurrentHashMap<>();
            log.info("Rating buffer has been flushed");
        }
    }

    @PostConstruct
    private void fillAvgRatingsCache() {
        List<AvgRating> ratingDtos = (List<AvgRating>) ratingDao.fidAllAvgRatingsGroupedMovie();
        Objects.requireNonNull(ratingDtos);
        avgRatingsCache = ratingDtos.stream().collect(Collectors.toConcurrentMap(AvgRating::getMovieId, avgRatingDto -> avgRatingDto));
    }
}
