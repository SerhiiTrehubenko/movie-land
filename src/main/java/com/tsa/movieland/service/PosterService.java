package com.tsa.movieland.service;

public interface PosterService {
    void add(int movieId, String posterLink);
    void update(int movieId, String posterLink);
}
