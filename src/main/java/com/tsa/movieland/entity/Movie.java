package com.tsa.movieland.entity;

import lombok.Builder;

import java.util.List;
@Builder
public record Movie(int id,
                    String nameRussian,
                    String nameNative,
                    int yearOfRelease,
                    double rating,
                    double price,
                    List<String> picturePath) {
}
