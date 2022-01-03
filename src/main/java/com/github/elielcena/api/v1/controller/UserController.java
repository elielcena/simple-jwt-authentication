package com.github.elielcena.api.v1.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.github.elielcena.api.v1.mapper.UserMapper;
import com.github.elielcena.api.v1.model.request.ChangePasswordRequest;
import com.github.elielcena.api.v1.model.request.UserRequest;
import com.github.elielcena.api.v1.model.response.UserResponse;
import com.github.elielcena.domain.model.User;
import com.github.elielcena.domain.service.UserService;
import com.github.elielcena.infrastructure.util.Constants;

/**
 * @author elielcena
 *
 */
@RestController
@RequestMapping(value = Constants.API_V1 + "/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper mapper;

    @GetMapping
    public List<UserResponse> findAll(User filtro) {
        return mapper.toCollectionModel(userService.findAll());
    }

    @GetMapping("/{id}")
    public UserResponse findById(@PathVariable Long id) {
        return mapper.toModel(userService.findById(id));
    }

    @PutMapping("/{id}")
    public UserResponse update(@PathVariable Long id, @RequestBody @Valid UserRequest request) {
        User user = userService.findById(id);
        mapper.copyToDomainEntity(request, user);
        return mapper.toModel(userService.save(user));
    }

    @PutMapping("/{id}/alterar-senha")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void changePassword(@PathVariable Long id, @RequestBody @Valid ChangePasswordRequest request) {
        userService.changePassword(id, request.getCurrentPassowrd(), request.getNewPassword(),
                request.getConfirmNewPassword());
    }

    // @CheckSecurity.Users.Edit
    // @PutMapping("/{id}/ativar")
    // @ResponseStatus(code = HttpStatus.NO_CONTENT)
    // public void ativar(@PathVariable Long id) {
    // userService.ativar(id);
    // }
    //
    // @CheckSecurity.Users.Edit
    // @DeleteMapping("/{id}/desativar")
    // @ResponseStatus(code = HttpStatus.NO_CONTENT)
    // public void desativar(@PathVariable Long id) {
    // userService.desativar(id);
    // }

}
