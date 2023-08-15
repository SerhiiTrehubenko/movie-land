package com.tsa.movieland.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class AddUpdateMovieDto {
    private String nameRussian;
    private String nameNative;
    private Integer yearOfRelease;
    private String description;
    private Double price;
    private String picturePath;
    private List<Integer> countries;
    private List<Integer> genres;
}
