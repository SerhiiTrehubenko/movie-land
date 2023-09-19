package com.tsa.movieland.mapper;

import com.tsa.movieland.dto.GenreDto;
import com.tsa.movieland.entity.GenreEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GenreMapper {
    GenreDto toGenreDto(GenreEntity genre);
}
