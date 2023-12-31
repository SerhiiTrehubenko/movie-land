package com.tsa.movieland.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Movie {
    private int id;
    private String nameRussian;
    private String nameNative;
    private Integer yearOfRelease;
    private String description;
    private Double price;
}
