package com.github.elielcena.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.elielcena.domain.exception.ResourceNotFoundException;
import com.github.elielcena.domain.model.Role;
import com.github.elielcena.domain.model.RoleEnum;
import com.github.elielcena.domain.repository.RoleRepository;

/**
 * @author elielcena
 *
 */
@Service
public class RoleService {

    private static final String MSG_RESOURCE_NOT_FOUND = "Role is not found.";

    @Autowired
    private RoleRepository roleRepository;

    public Role findById(Long id) {
        return roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(MSG_RESOURCE_NOT_FOUND));
    }

    public Role findByName(RoleEnum role) {
        return roleRepository.findByName(role).orElseThrow(() -> new ResourceNotFoundException(MSG_RESOURCE_NOT_FOUND));
    }

}
