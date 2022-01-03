package com.github.elielcena.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.elielcena.domain.model.Role;
import com.github.elielcena.domain.model.RoleEnum;

/**
 * @author elielcena
 *
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleEnum name);
}
