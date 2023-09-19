package com.tsa.movieland.mapper;

import com.tsa.movieland.entity.Country;
import com.tsa.movieland.entity.CountryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CountryMapper {
    Country toCountryDto(CountryEntity entity);
}
