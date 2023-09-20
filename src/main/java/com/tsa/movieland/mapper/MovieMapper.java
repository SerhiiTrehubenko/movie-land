package com.tsa.movieland.mapper;

import com.tsa.movieland.dto.AddUpdateMovieDto;
import com.tsa.movieland.dto.MovieByIdDto;
import com.tsa.movieland.entity.Movie;
import com.tsa.movieland.dto.MovieFindAllDto;
import com.tsa.movieland.entity.Poster;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MovieMapper {

    @Mapping(source = "posters", target = "picturePath", qualifiedByName = "posterToString")
    MovieFindAllDto toMovieFindAllDto(Movie entity);

    @Named("posterToString")
    default List<String> posterToString(List<Poster> posterEntities) {
        return posterEntities.stream().map(Poster::getLink).toList();
    }

    @Mapping(source = "posters", target = "picturePath", qualifiedByName = "posterToString")
    MovieByIdDto toMovieByIdDto(Movie entity);

    Movie toMovie(AddUpdateMovieDto movieDto);

    @Mapping( target = "nameRussian", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "nameNative", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping( target = "yearOfRelease", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping( target = "description", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping( target = "price", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Movie updateMovie(@MappingTarget Movie movieEntity, AddUpdateMovieDto movieDto);
}
