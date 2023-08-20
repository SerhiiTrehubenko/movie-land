package com.tsa.movieland.controller;

import com.tsa.movieland.entity.Country;
import com.tsa.movieland.service.CountryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/country", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequiredArgsConstructor
public class CountryController {

    private final CountryService countryService;

    @GetMapping
    public Iterable<Country> getAllCountries() {
        Iterable<Country> countries = countryService.findAll();
        log.info("Query get all Countries");
        return countries;
    }
}
