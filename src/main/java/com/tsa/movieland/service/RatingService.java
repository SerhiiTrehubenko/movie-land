package com.tsa.movieland.service;

import com.tsa.movieland.common.RatingRequest;

public interface RatingService {
    void addRating(RatingRequest ratingRequest);
    double getAvgRate(int movieID);
}
