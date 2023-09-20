package com.tsa.movieland.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
@Builder
@Getter
public class MovieFindAllDto {
    private int id;
    private String nameRussian;
    private String nameNative;
    private int yearOfRelease;
    private double rating;
    private double price;
    private List<String> picturePath;

    public void setRating(double rating) {
        this.rating = rating;
    }
}
