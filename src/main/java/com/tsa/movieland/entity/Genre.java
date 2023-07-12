package com.tsa.movieland.entity;

import lombok.Builder;

@Builder
public record Genre(int id, String name) {
}
