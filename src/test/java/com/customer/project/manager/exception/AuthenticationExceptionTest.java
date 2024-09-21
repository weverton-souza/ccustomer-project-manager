package com.customer.project.manager.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("AuthenticationException Tests")
class AuthenticationExceptionTest {

    @Test
    @DisplayName("Should create exception with correct message")
    void testAuthenticationException() {
        String errorMessage = "Invalid credentials";

        AuthenticationException exception = assertThrows(AuthenticationException.class, () -> {
            throw new AuthenticationException(errorMessage);
        });

        assertEquals(errorMessage, exception.getMessage());
    }
}
