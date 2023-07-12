package com.tsa.movieland.controller;

import com.tsa.movieland.entity.Genre;
import com.tsa.movieland.service.GenreService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class GenreController {


    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("/genre")
    public Iterable<Genre> findAll() {
        return genreService.findAll();
    }
}
