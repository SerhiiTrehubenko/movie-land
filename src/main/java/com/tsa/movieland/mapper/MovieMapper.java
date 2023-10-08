package com.tsa.movieland.mapper;

import com.tsa.movieland.dto.AddUpdateMovieDto;
import com.tsa.movieland.dto.MovieByIdDto;
import com.tsa.movieland.entity.Movie;
import com.tsa.movieland.dto.MovieFindAllDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MovieMapper {

    @Mapping(source = "posters", target = "picturePath")
    MovieFindAllDto toMovieFindAllDto(Movie entity);

    @Mapping(source = "posters", target = "picturePath")
    MovieByIdDto toMovieByIdDto(Movie entity);

    @Mapping(target = "posters", expression = "java(null == movieDto.getPicturePath() ? null : java.util.Arrays.asList(movieDto.getPicturePath()))")
    Movie toMovie(AddUpdateMovieDto movieDto);

    @Mapping(target = "nameRussian", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "nameNative", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "yearOfRelease", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "description", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "price", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateMovie(@MappingTarget Movie movieEntity, AddUpdateMovieDto movieDto);

    MovieByIdDto copy(MovieByIdDto movieDto);
}
