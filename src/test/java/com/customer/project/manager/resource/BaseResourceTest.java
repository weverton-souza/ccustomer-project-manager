package com.customer.project.manager.resource;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("BaseResource Tests")
class BaseResourceTest {

    private final BaseResource baseResource = new BaseResource() {};

    @Test
    @DisplayName("Should retrieve pageable parameters when they are present and valid")
    void testRetrievePageableParameterWithValidParams() {
        Map<String, String> params = new HashMap<>();
        params.put("pageNumber", "2");
        params.put("pageSize", "10");

        PageRequest pageRequest = baseResource.retrievePageableParameter(params);

        assertEquals(2, pageRequest.getPageNumber());
        assertEquals(10, pageRequest.getPageSize());
    }

    @Test
    @DisplayName("Should return default values when pagination parameters are missing")
    void testRetrievePageableParameterWithMissingParams() {
        Map<String, String> params = new HashMap<>();

        PageRequest pageRequest = baseResource.retrievePageableParameter(params);

        assertEquals(0, pageRequest.getPageNumber());
        assertEquals(15, pageRequest.getPageSize());
    }

    @Test
    @DisplayName("Should return default values when pagination parameters are present but empty")
    void testRetrievePageableParameterWithEmptyParams() {
        Map<String, String> params = new HashMap<>();
        params.put("pageNumber", "");
        params.put("pageSize", "");

        PageRequest pageRequest = baseResource.retrievePageableParameter(params);

        assertEquals(0, pageRequest.getPageNumber());
        assertEquals(15, pageRequest.getPageSize());
    }

    @Test
    @DisplayName("Should handle case when one pagination parameter is missing")
    void testRetrievePageableParameterWithOneMissingParam() {
        Map<String, String> params = new HashMap<>();
        params.put("pageNumber", "3");

        PageRequest pageRequest = baseResource.retrievePageableParameter(params);

        assertEquals(3, pageRequest.getPageNumber());
        assertEquals(15, pageRequest.getPageSize());
    }

    @Test
    @DisplayName("Should throw NumberFormatException when pagination parameters are invalid")
    void testRetrievePageableParameterWithInvalidParams() {
        Map<String, String> params = new HashMap<>();
        params.put("pageNumber", "invalid");
        params.put("pageSize", "invalid");

        try {
            baseResource.retrievePageableParameter(params);
        } catch (NumberFormatException e) {
            assertEquals("For input string: \"invalid\"", e.getMessage());
        }
    }
}
