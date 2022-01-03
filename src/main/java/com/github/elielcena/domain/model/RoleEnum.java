package com.github.elielcena.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author elielcena
 *
 */
@AllArgsConstructor
@Getter
public enum RoleEnum {

    ROLE_USER("USER"),
    ROLE_MODERATOR("MODARADOR"),
    ROLE_ADMIN("ADMIN");

    private String description;

}
