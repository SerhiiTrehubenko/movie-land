package com.tsa.movieland.entity;

import java.util.List;

public record Movie(int id,
                    String nameRussian,
                    String nameNative,
                    int yearOfRelease,
                    double rating,
                    double price,
                    List<String> picturePath) {
}
