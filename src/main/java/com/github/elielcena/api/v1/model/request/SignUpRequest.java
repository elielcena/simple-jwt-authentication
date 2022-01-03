package com.github.elielcena.api.v1.model.request;

import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.github.elielcena.domain.model.RoleEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author elielcena
 *
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private Set<RoleEnum> roles;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

}
