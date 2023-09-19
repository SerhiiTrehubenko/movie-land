package com.tsa.movieland.mapper;

import com.tsa.movieland.dto.PosterDto;
import com.tsa.movieland.entity.PosterEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PosterMapper {
    PosterDto toPosterDto(PosterEntity posterEntity);
}
