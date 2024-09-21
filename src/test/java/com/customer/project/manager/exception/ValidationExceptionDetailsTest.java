package com.customer.project.manager.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("ValidationExceptionDetails Tests")
class ValidationExceptionDetailsTest {

    @Test
    @DisplayName("Should build ValidationExceptionDetails with correct attributes")
    void testValidationExceptionDetailsBuilder() {
        List<FieldExceptionDetails> fieldDetails = List.of(new FieldExceptionDetails("field", List.of("error1", "error2")));
        String timestamp = "21/09/2024 18:30:00";
        String developerMessage = "Validation Error";

        ValidationExceptionDetails validationExceptionDetails = ValidationExceptionDetails.Builder()
                .withFieldExceptionDetails(fieldDetails)
                .withTimestamp(timestamp)
                .withDeveloperMessage(developerMessage)
                .build();

        assertEquals(fieldDetails, validationExceptionDetails.getFieldExceptionDetails());
        assertEquals(timestamp, validationExceptionDetails.getTimestamp());
        assertEquals(developerMessage, validationExceptionDetails.getDeveloperMessage());
    }
}
