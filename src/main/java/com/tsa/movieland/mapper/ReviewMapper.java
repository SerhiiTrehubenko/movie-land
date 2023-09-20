package com.tsa.movieland.mapper;

import com.tsa.movieland.dto.ReviewDto;
import com.tsa.movieland.entity.Review;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    ReviewDto toReviewDto(Review reviewEntity);
}
