package com.tsa.movieland.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Country {
    private int id;
    private String name;
}
