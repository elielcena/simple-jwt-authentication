package com.github.elielcena.api.v1.model.request;

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
public class ChangePasswordRequest {

    private String currentPassowrd;

    private String newPassword;

    private String confirmNewPassword;
}
