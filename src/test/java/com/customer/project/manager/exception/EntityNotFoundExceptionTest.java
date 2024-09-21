package com.customer.project.manager.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("EntityNotFoundException Tests")
class EntityNotFoundExceptionTest {

    @Test
    @DisplayName("Should create exception with correct message")
    void testEntityNotFoundException() {
        String errorMessage = "Entity not found";

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            throw new EntityNotFoundException(errorMessage);
        });

        assertEquals(errorMessage, exception.getMessage());
    }
}
