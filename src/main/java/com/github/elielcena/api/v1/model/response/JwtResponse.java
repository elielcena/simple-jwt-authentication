package com.github.elielcena.api.v1.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * @author elielcena
 *
 */
@SuperBuilder
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class JwtResponse extends UserResponse {

    @Builder.Default
    private String type = "Bearer";

    private String token;

}
