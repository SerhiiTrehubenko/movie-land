package com.tsa.movieland.entity;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Genre implements Cloneable {
    private int id;
    private String name;


    @Override
    public Genre clone() {
        return Genre.builder().id(id).name(name).build();
    }


}
