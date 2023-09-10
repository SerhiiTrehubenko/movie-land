package com.tsa.movieland.dao;

import com.tsa.movieland.common.RatingRequest;
import com.tsa.movieland.common.AvgRating;

import java.util.Iterator;


public interface RatingDao {
    Iterable<AvgRating> fidAllAvgRatingsGroupedMovie();

    void saveBuffer(Iterator<RatingRequest> iterator);
}
