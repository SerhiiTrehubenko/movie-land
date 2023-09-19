package com.tsa.movieland.mapper;

import com.tsa.movieland.entity.Review;
import com.tsa.movieland.entity.ReviewEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    Review toReviewDto(ReviewEntity reviewEntity);
}
