package com.tsa.movieland.dto;

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
    private Iterable<CountryDto> countries;
    private Iterable<GenreDto> genres;
    private Iterable<ReviewDto> reviews;
}
