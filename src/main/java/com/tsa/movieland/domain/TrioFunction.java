package com.tsa.movieland.domain;

@FunctionalInterface
public interface TrioFunction<T, U, Y, R> {
    R apply(T t, U u, Y y);
}
