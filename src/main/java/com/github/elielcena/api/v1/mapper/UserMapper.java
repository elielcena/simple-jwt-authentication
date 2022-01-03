package com.github.elielcena.api.v1.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.elielcena.api.v1.model.request.ChangePasswordRequest;
import com.github.elielcena.api.v1.model.request.UserRequest;
import com.github.elielcena.api.v1.model.response.UserResponse;
import com.github.elielcena.domain.model.User;

/**
 * @author elielcena
 *
 */
@Component
public class UserMapper {

    @Autowired
    private ModelMapper modelMapper;

    public UserResponse toModel(User entity) {
        return modelMapper.map(entity, UserResponse.class);
    }

    public List<UserResponse> toCollectionModel(List<User> entities) {
        return entities.stream().map(this::toModel).collect(Collectors.toList());
    }

    public User toDomainEntity(UserRequest entity) {
        return modelMapper.map(entity, User.class);
    }

    public User toDomainEntityPassword(ChangePasswordRequest entity) {
        return modelMapper.map(entity, User.class);
    }

    public void copyToDomainEntity(UserRequest entityRequest, User entity) {
        modelMapper.map(entityRequest, entity);
    }

}
