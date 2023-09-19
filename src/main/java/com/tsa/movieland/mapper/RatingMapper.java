package com.tsa.movieland.mapper;

import com.tsa.movieland.common.AvgRating;
import com.tsa.movieland.common.RatingRequest;
import com.tsa.movieland.entity.Rating;
import com.tsa.movieland.repository.RatingRepository;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RatingMapper {
    AvgRating toAvgRating(RatingRepository.AverageRating rating);
    Rating toRating(RatingRequest ratingRequest);
}
