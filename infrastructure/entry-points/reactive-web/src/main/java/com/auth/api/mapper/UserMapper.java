package com.auth.api.mapper;

import com.auth.api.dto.UserRequest;
import com.auth.api.dto.UserResponse;
import com.auth.model.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toModel(UserRequest userRequest);

    UserResponse toResponse(User user);

}
