package com.tsa.movieland.mapper;

import com.tsa.movieland.dto.PosterDto;
import com.tsa.movieland.entity.Poster;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PosterMapper {
    PosterDto toPosterDto(Poster posterEntity);
}
