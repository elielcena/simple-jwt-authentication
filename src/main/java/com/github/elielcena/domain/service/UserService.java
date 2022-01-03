package com.github.elielcena.domain.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.github.elielcena.domain.exception.BusinessException;
import com.github.elielcena.domain.exception.ResourceNotFoundException;
import com.github.elielcena.domain.model.Role;
import com.github.elielcena.domain.model.RoleEnum;
import com.github.elielcena.domain.model.User;
import com.github.elielcena.domain.repository.UserRepository;

/**
 * @author elielcena
 *
 */
@Service
public class UserService {

    private static final String MSG_RESOURCE_NOT_FOUND = "There is no user register with code %d";

    private static final String MSG_RESOURCE_IN_USE = "%s %s is already in use!";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(MSG_RESOURCE_NOT_FOUND, id)));
    }

    public User save(User user) {
        var existUsername = findByUsername(user.getUsername());
        var existEmail = findByEmail(user.getEmail());

        if (existUsername.isPresent() && !existUsername.get().equals(user))
            throw new BusinessException(String.format(MSG_RESOURCE_IN_USE, "Username", user.getUsername()));

        if (existEmail.isPresent() && !existEmail.get().equals(user))
            throw new BusinessException(String.format(MSG_RESOURCE_IN_USE, "Email", user.getEmail()));

        if (user.isNew()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        if (CollectionUtils.isEmpty(user.getRoles())) {
            Role userRole = roleService.findByName(RoleEnum.ROLE_USER);
            user.setRoles(new HashSet<>());
            user.getRoles().add(userRole);
        }

        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void addRoleToUser(Long idUser, Long idRole) {
        addOrRemoveRole(Boolean.TRUE, idUser, idRole);
    }

    public void removeRoleToUser(Long idUser, Long idRole) {
        addOrRemoveRole(Boolean.FALSE, idUser, idRole);
    }

    public void changePassword(Long id, String currentPassword, String newPassword, String confirmPassword) {
        User user = findById(id);

        validCurrentpassword(user, currentPassword);
        validNewPasswords(newPassword, confirmPassword);

        user.setPassword(newPassword);

        save(user);
    }

    private void validCurrentpassword(User user, String currentPassword) {
        if (!passwordEncoder.matches(currentPassword, user.getPassword()))
            throw new BusinessException("The current password is not valid");
    }

    private void validNewPasswords(String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword))
            throw new BusinessException("New passwords do not match");
    }

    private void addOrRemoveRole(Boolean isAdd, Long idUser, Long idRole) {
        User user = findById(idUser);
        Role role = roleService.findById(idRole);

        user.addOrRemoveRole(isAdd, role);

        save(user);
    }

}
