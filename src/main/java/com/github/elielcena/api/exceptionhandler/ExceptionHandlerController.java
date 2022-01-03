package com.github.elielcena.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import com.github.elielcena.domain.exception.BusinessException;
import com.github.elielcena.domain.exception.EntityInUseException;
import com.github.elielcena.domain.exception.ResourceNotFoundException;

import lombok.extern.slf4j.Slf4j;

/**
 * Treats all possible exceptions that you may give when requesting the
 * available endpoints.
 *
 * @author elielcena
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    /**
     * Provides handling the Exception exception.
     * 
     * @param ex the exception
     * @param request the current request
     * @return ErrorResponse
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUncaught(Exception ex, WebRequest request) {
        String message = "An unexpected internal system error has occurred. Try again and if the problem persists, contact your system administrator";
        log.error(ex.getMessage(), ex);
        return createErrorResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR, request, message).build();
    }

    /**
     * Provides handling the BusinessException exception.
     * 
     * @param ex the exception
     * @param request the current request
     * @return ErrorResponse
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBusiness(BusinessException ex, WebRequest request) {
        return createErrorResponseBuilder(HttpStatus.BAD_REQUEST, request, ex.getMessage()).build();
    }

    /**
     * Provides handling the ResourceNotFoundException exception.
     * 
     * @param ex the exception
     * @param request the current request
     * @return ErrorResponse
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorResponse handleEntityNotFound(BusinessException ex, WebRequest request) {
        return createErrorResponseBuilder(HttpStatus.NOT_FOUND, request, ex.getMessage()).build();
    }

    /**
     * Provides handling the EntityInUseException exception.
     * 
     * @param ex the exception
     * @param request the current request
     * @return ErrorResponse
     */
    @ExceptionHandler(EntityInUseException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorResponse handleEntityInUse(EntityInUseException ex, WebRequest request) {
        String message = "This record cannot be removed as it is in use";
        return createErrorResponseBuilder(HttpStatus.CONFLICT, request, message).build();
    }

    /**
     * Provides handling the AccessDeniedException exception.
     * 
     * @param ex the exception
     * @param request the current request
     * @return ErrorResponse
     */
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ErrorResponse handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        String message = "Access denied";
        return createErrorResponseBuilder(HttpStatus.FORBIDDEN, request, message).build();
    }

    /**
     * Provides handling the BadCredentialsException exception.
     * 
     * @param ex the exception
     * @param request the current request
     * @return ErrorResponse
     */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadCredentialsException.class)
    public ErrorResponse handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        String message = "Incorrect username or password";
        return createErrorResponseBuilder(HttpStatus.BAD_REQUEST, request, message).build();
    }

    /**
     * Provides handling the HttpMessageNotReadableException exception.
     * 
     * @param ex the exception
     * @param headers the headers for the response
     * @param status the response status
     * @param request the current request
     * @return ResponseEntity<Object>
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {

        Throwable rootCause = ExceptionUtils.getRootCause(ex);

        if (rootCause instanceof PropertyBindingException)
            return handlePropertyBindingException((PropertyBindingException) rootCause, headers, status, request);
        else if (rootCause instanceof InvalidFormatException)
            return handleInvalidFormatException((InvalidFormatException) rootCause, headers, status, request);

        String message = "The request body is invalid. Check the syntax error";
        Exception exception = new Exception(message);

        return handleExceptionInternal(exception, null, headers, status, request);
    }

    /**
     * Provides handling the MethodArgumentNotValidException exception.
     * 
     * @param ex the exception
     * @param headers the headers for the response
     * @param status the response status
     * @param request the current request
     * @return ResponseEntity<Object>
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {

        return handleValidationInternal(ex, headers, status, request, ex.getBindingResult());
    }

    /**
     * Provides handling the BindException exception.
     * 
     * @param ex the exception
     * @param body the body for the response
     * @param headers the headers for the response
     * @param status the response status
     * @param request the current request
     * @return ResponseEntity<Object>
     */
    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status,
            WebRequest request) {

        return handleValidationInternal(ex, headers, status, request, ex.getBindingResult());
    }

    /**
     * Provides handling the TypeMismatchException exception.
     * 
     * @param ex the exception
     * @param headers the headers for the response
     * @param status the response status
     * @param request the current request
     * @return ResponseEntity<Object>
     */
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        if (ex instanceof MethodArgumentTypeMismatchException) {
            return handleMethodArgumentTypeMismatch((MethodArgumentTypeMismatchException) ex, headers, status, request);
        }

        return super.handleTypeMismatch(ex, headers, status, request);
    }

    /**
     * Provides handling the HttpMediaTypeNotAcceptableException exception.
     * 
     * @param ex the exception
     * @param headers the headers for the response
     * @param status the response status
     * @param request the current request
     * @return ResponseEntity<Object>
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        return ResponseEntity.status(status).headers(headers).build();
    }

    /**
     * A single place to customize the response body of all exception types.
     * 
     * @param ex the exception
     * @param body the body for the response
     * @param headers the headers for the response
     * @param status the response status
     * @param request the current request
     * @return ResponseEntity<Object>
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        var responseBody = createErrorResponseBuilder(status, request, ex.getLocalizedMessage()).build();
        log.error(ex.getMessage(), ex);

        return super.handleExceptionInternal(ex, responseBody, headers, status, request);
    }

    /**
     * Provides handling the InvalidFormatException exception.
     * 
     * @param ex the exception
     * @param headers the headers for the response
     * @param status the response status
     * @param request the current request
     * @return ResponseEntity<Object>
     */
    private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        String path = ex.getPath().stream().map(Reference::getFieldName).collect(Collectors.joining("."));

        String message = "The property '%s' was given the value '%s', which is of an invalid type. Correct and enter a value compatible with type %s";
        Exception exception = new Exception(
                String.format(message, path, ex.getValue(), ex.getTargetType().getSimpleName()));

        return handleExceptionInternal(exception, null, headers, status, request);
    }

    /**
     * Customize the response body of the PropertyBindingException exception.
     * 
     * @param ex the exception
     * @param headers the headers for the response
     * @param status the response status
     * @param request the current request
     * @return ResponseEntity<Object>
     */
    private ResponseEntity<Object> handlePropertyBindingException(PropertyBindingException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        String message = "The property '%s' is not compatible with the expected body for the request. Remove it to proceed";
        Exception exception = new Exception(String.format(message, ex.getPropertyName()));

        return handleExceptionInternal(exception, null, headers, status, request);
    }

    /**
     * Customize the response body of the MethodArgumentTypeMismatchException
     * exception.
     * 
     * @param ex the exception
     * @param headers the headers for the response
     * @param status the response status
     * @param request the current request
     * @return ResponseEntity<Object>
     */
    private ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {

        String message = "The URL parameter '%s' called the value '%s', which is an invalid type. Correct and enter a value compatible with type %s";
        Exception exception = new Exception(
                String.format(message, ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()));

        return handleExceptionInternal(exception, null, headers, status, request);
    }

    /**
     * Customize the response body of the MethodArgumentTypeMismatchException
     * exception.
     * 
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    private ResponseEntity<Object> handleValidationInternal(Exception ex, HttpHeaders headers, HttpStatus status,
            WebRequest request, BindingResult bindingResult) {
        var fields = bindingResult.getFieldErrors().stream().map(error -> {
            // String message = messageSource.getMessage(error, new Locale("pt", "BR"));
            String message = messageSource.getMessage(error, Locale.ENGLISH);
            return new ErrorResponse.Cause(error.getField(), message);
        }).distinct().collect(Collectors.toList());

        String message = "One or more fields are invalid";
        var responseBody = createErrorResponseBuilder(status, request, message).causes(fields).build();

        return super.handleExceptionInternal(ex, responseBody, headers, status, request);
    }

    /**
     * Generates a builder instance of the ErrorResponse class.
     * 
     * @param status the response status
     * @param request the current request
     * @param message the message for the response
     * @return ErrorResponse.ErrorResponseBuilder
     */
    private ErrorResponse.ErrorResponseBuilder createErrorResponseBuilder(HttpStatus status,
            WebRequest request, String message) {
        return ErrorResponse.builder().timestamp(OffsetDateTime.now()).status(status.value())
                .error(status.getReasonPhrase()).message(message).path(getPath(request));
    }

    /**
     * Handles and returns request uri.
     * 
     * @param status the response status
     * @param request the current request
     * @param message the message for the response
     * @return String
     */
    private String getPath(WebRequest request) {
        return ((ServletWebRequest) request).getRequest().getRequestURI().toString();
    }

}
