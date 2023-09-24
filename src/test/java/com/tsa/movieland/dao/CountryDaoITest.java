package com.tsa.movieland.dao;

import com.tsa.movieland.CommonContainer;
import com.tsa.movieland.dto.CountryDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class CountryDaoITest extends CommonContainer {

    @Autowired
    CountryDao countryDao;

    @Test
    void shouldReturnCountriesByMovieId() {
        List<CountryDto> countries = (List<CountryDto>) countryDao.findByMovieId(1121);
        assertEquals(4, countries.size());
        assertEquals(501, countries.get(0).getId());
        assertEquals("США", countries.get(0).getName());
        assertEquals(504, countries.get(1).getId());
        assertEquals("Италия", countries.get(1).getName());
        assertEquals(505, countries.get(2).getId());
        assertEquals("Германия", countries.get(2).getName());
        assertEquals(507, countries.get(3).getId());
        assertEquals("Испания", countries.get(3).getName());
    }
}