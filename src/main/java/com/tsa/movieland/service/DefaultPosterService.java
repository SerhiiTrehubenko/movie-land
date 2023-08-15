package com.tsa.movieland.service;

import com.tsa.movieland.dao.PosterDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultPosterService implements PosterService {

    private  final PosterDao posterDao;

    @Override
    public void addPoster(int movieId, String posterLink) {
        posterDao.addPoster(movieId, posterLink);
    }
}
