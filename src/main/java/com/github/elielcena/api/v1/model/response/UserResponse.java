package com.github.elielcena.api.v1.model.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author elielcena
 *
 */
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private Long id;

    private String username;

    private String email;

    private List<String> roles;

}
