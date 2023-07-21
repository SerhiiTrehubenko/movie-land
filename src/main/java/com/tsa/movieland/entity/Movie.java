package com.tsa.movieland.entity;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
@Builder
@Getter
public class Movie {
    private int id;
    private String nameRussian;
    private String nameNative;
    private int yearOfRelease;
    private double rating;
    private double price;
    private List<String> picturePath;
}
