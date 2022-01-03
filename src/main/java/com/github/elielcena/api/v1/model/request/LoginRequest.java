package com.github.elielcena.api.v1.model.request;

import javax.validation.constraints.NotBlank;

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
public class LoginRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

}
