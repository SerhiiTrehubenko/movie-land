package com.tsa.movieland.copier;

import com.tsa.movieland.context.Copier;
import com.tsa.movieland.dto.MovieByIdDto;

@Copier
public class MovieByIdCopier {
    public MovieByIdDto copy(MovieByIdDto movie) {
        return MovieByIdDto.builder()
                .id(movie.getId())
                .nameRussian(movie.getNameRussian())
                .nameNative(movie.getNameNative())
                .yearOfRelease(movie.getYearOfRelease())
                .description(movie.getDescription())
                .rating(movie.getRating())
                .price(movie.getPrice())
                .picturePath(movie.getPicturePath())
                .countries(movie.getCountries())
                .genres(movie.getGenres())
                .reviews(movie.getReviews())
                .build();
    }
}
