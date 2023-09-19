package com.tsa.movieland.mapper;

import com.tsa.movieland.entity.User;
import com.tsa.movieland.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserEntity userEntity);
}
