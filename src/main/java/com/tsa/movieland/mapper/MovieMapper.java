package com.tsa.movieland.mapper;

import com.tsa.movieland.dto.AddUpdateMovieDto;
import com.tsa.movieland.dto.MovieByIdDto;
import com.tsa.movieland.entity.MovieEntity;
import com.tsa.movieland.entity.MovieFindAllDto;
import com.tsa.movieland.entity.PosterEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MovieMapper {

    @Mapping(source = "posters", target = "picturePath", qualifiedByName = "posterToString")
    MovieFindAllDto toMovieFindAllDto(MovieEntity entity);

    @Named("posterToString")
    default List<String> posterToString(List<PosterEntity> posterEntities) {
        return posterEntities.stream().map(PosterEntity::getLink).toList();
    }

    @Mapping(source = "posters", target = "picturePath", qualifiedByName = "posterToString")
    MovieByIdDto toMovieByIdDto(MovieEntity entity);

    MovieEntity toMovie(AddUpdateMovieDto movieDto);

    @Mapping( target = "nameRussian", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "nameNative", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping( target = "yearOfRelease", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping( target = "description", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping( target = "price", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    MovieEntity updateMovie(@MappingTarget MovieEntity movieEntity, AddUpdateMovieDto movieDto);
}
