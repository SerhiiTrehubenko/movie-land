package com.tsa.movieland.dao;

import com.tsa.movieland.common.RatingRequest;
import com.tsa.movieland.common.AvgRating;

import java.util.Iterator;
import java.util.Set;


public interface RatingDao {
    Iterable<AvgRating> fidAllAvgRatingsGroupedMovie();

    void saveBuffer(Iterator<RatingRequest> iterator);

    void saveBuffer(Set<RatingRequest> avgRatings);
}
