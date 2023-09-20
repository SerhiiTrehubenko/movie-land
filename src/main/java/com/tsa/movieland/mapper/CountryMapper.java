package com.tsa.movieland.mapper;

import com.tsa.movieland.dto.CountryDto;
import com.tsa.movieland.entity.Country;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CountryMapper {
    CountryDto toCountryDto(Country entity);
}
