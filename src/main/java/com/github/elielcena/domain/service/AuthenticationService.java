package com.github.elielcena.domain.service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;

import com.github.elielcena.api.v1.model.request.LoginRequest;
import com.github.elielcena.api.v1.model.request.SignUpRequest;
import com.github.elielcena.api.v1.model.response.JwtResponse;
import com.github.elielcena.core.security.jwt.JwtUtils;
import com.github.elielcena.core.security.service.UserDetailsImpl;
import com.github.elielcena.domain.model.User;

/**
 * @author elielcena
 *
 */
@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    public JwtResponse signIn(LoginRequest loginRequest) {
        var authToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(authToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return JwtResponse.builder()
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .token(jwt)
                .roles(roles)
                .build();
    }

    public JwtResponse signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        User user = User.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(signUpRequest.getPassword())
                .build();

        if (!CollectionUtils.isEmpty(signUpRequest.getRoles())) {
            user.setRoles(new HashSet<>());
            signUpRequest.getRoles().forEach(role -> user.getRoles().add(roleService.findByName(role)));
        }

        userService.save(user);

        var loginRequest = LoginRequest.builder().username(signUpRequest.getUsername()).password(signUpRequest.getPassword()).build();
        return signIn(loginRequest);
    }

}
