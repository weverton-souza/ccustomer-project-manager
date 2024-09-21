package com.customer.project.manager.exception;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("RestExceptionHandler Tests")
class RestExceptionHandlerTest {

    private final RestExceptionHandler handler = new RestExceptionHandler();

    @Test
    @DisplayName("Should handle ResourceNotFoundException with correct status code")
    void testHandleResourceNotFoundException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource not found");

        ResponseEntity<Object> response = handler.handleResourceNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Should handle SerializationError with correct status code")
    void testHandleSerializationError() {
        HttpMessageNotWritableException exception = new HttpMessageNotWritableException("Serialization error occurred");

        ResponseEntity<ExceptionDetails> response = handler.handleSerializationError(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException triggering else branch")
    void testHandleMethodArgumentNotValidElseBranch() throws NoSuchMethodException {
        BindingResult bindingResult = mock(BindingResult.class);
        List<FieldError> fieldErrors = new ArrayList<>();

        fieldErrors.add(new FieldError("objectName", "fieldName", "Error message 1"));
        fieldErrors.add(new FieldError("objectName", "fieldName", "Error message 2"));

        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        Method method = this.getClass().getDeclaredMethod("testHandleMethodArgumentNotValidElseBranch");
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(
                new org.springframework.core.MethodParameter(method, -1), bindingResult
        );

        ResponseEntity<Object> response = handler.handleMethodArgumentNotValid(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ValidationExceptionDetails details = (ValidationExceptionDetails) response.getBody();
        assert details != null;
        assertEquals(1, details.getFieldExceptionDetails().size());
        assertEquals("fieldName", details.getFieldExceptionDetails().getFirst().getField());
        assertEquals(2, details.getFieldExceptionDetails().getFirst().getFieldMessages().size());
        assertEquals("Error message 1", details.getFieldExceptionDetails().getFirst().getFieldMessages().get(0));
        assertEquals("Error message 2", details.getFieldExceptionDetails().getFirst().getFieldMessages().get(1));
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException with correct status code and message")
    void testHandleMethodArgumentNotValid() throws NoSuchMethodException {
        BindingResult bindingResult = mock(BindingResult.class);
        List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(new FieldError("objectName", "field", "must not be blank"));
        fieldErrors.add(new FieldError("objectName", "anotherField", "must be valid"));

        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        Method method = this.getClass().getDeclaredMethod("testHandleMethodArgumentNotValid");
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(
                new org.springframework.core.MethodParameter(method, -1), bindingResult
        );

        ResponseEntity<Object> response = handler.handleMethodArgumentNotValid(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ValidationExceptionDetails details = (ValidationExceptionDetails) response.getBody();
        assert details != null;
        assertEquals(2, details.getFieldExceptionDetails().size());
        assertEquals("must not be blank", details.getFieldExceptionDetails().get(0).getFieldMessages().getFirst());
        assertEquals("must be valid", details.getFieldExceptionDetails().get(1).getFieldMessages().getFirst());
    }

    @Test
    @DisplayName("Should handle all uncaught exceptions with correct status code")
    void testHandleAllUncaughtException() {
        Exception exception = new Exception("An unexpected error occurred");
        WebRequest request = mock(WebRequest.class);

        ResponseEntity<Object> response = handler.handleAllUncaughtException(exception, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
