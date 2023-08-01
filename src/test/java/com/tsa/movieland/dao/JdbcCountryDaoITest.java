package com.tsa.movieland.dao;

import com.tsa.movieland.entity.Country;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JdbcCountryDaoITest extends DaoBaseTest {

    @Autowired
    CountryDao countryDao;

    @Test
    void souldReturnCountriesByMovieId() {
        List<Country> countries = (List<Country>) countryDao.findByMovieId(1121);
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