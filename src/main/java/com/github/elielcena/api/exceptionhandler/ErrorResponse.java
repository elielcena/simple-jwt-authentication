package com.github.elielcena.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author elielcena
 *
 */
@JsonInclude(Include.NON_NULL)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {

    private OffsetDateTime timestamp;

    private Integer status;

    private String error;

    private String message;

    private List<Cause> causes;

    private String path;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Cause {

        private String name;

        private String message;
    }

}
