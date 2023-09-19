package com.tsa.movieland.controller;

import com.tsa.movieland.dto.GenreDto;
import com.tsa.movieland.service.GenreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class GenreController {


    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("/genre")
    public Iterable<GenreDto> findAll() {
        log.info("Query get all genres");
        return genreService.findAll();
    }
}
