package com.tsa.movieland.dto;

import com.tsa.movieland.entity.Country;
import com.tsa.movieland.entity.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@Setter
@ToString
public class MovieByIdDto {
    private int id;
    private String nameRussian;
    private String nameNative;
    private int yearOfRelease;
    private String description;
    private double rating;
    private double price;
    private List<String> picturePath;
    private Iterable<Country> countries;
    private Iterable<GenreDto> genres;
    private Iterable<Review> reviews;
}
