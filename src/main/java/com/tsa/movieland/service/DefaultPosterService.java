package com.tsa.movieland.service;

import com.tsa.movieland.dao.PosterDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DefaultPosterService implements PosterService {

    private  final PosterDao posterDao;

    @Override
    public void add(int movieId, String posterLink) {
        posterDao.add(movieId, posterLink);
    }

    @Override
    public void update(int movieId, String posterLink) {
        posterDao.update(movieId, posterLink);
    }
}
