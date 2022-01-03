package com.github.elielcena.api.v1.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.elielcena.api.v1.model.request.LoginRequest;
import com.github.elielcena.api.v1.model.request.SignUpRequest;
import com.github.elielcena.api.v1.model.response.JwtResponse;
import com.github.elielcena.domain.service.AuthenticationService;
import com.github.elielcena.infrastructure.util.Constants;

/**
 * @author elielcena
 *
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = Constants.API_V1 + "/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/signin")
    public JwtResponse signIn(@Valid @RequestBody LoginRequest loginRequest) {
        return authenticationService.signIn(loginRequest);
    }

    @PostMapping("/signup")
    public JwtResponse signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        return authenticationService.signUp(signUpRequest);
    }
}
