package com.tsa.movieland.dao;

import com.tsa.movieland.common.RatingRequest;
import com.tsa.movieland.common.AvgRating;

import java.util.List;
import java.util.Map;
import java.util.Set;


public interface RatingDao {
    Iterable<AvgRating> fidAllAvgRatingsGroupedMovie();

    void saveBuffer(Set<Map.Entry<String, List<RatingRequest>>> entrySet);
}
