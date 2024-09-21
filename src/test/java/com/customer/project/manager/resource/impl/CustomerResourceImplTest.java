package com.customer.project.manager.resource.impl;

import com.customer.project.manager.payload.customer.CustomerRequest;
import com.customer.project.manager.payload.customer.CustomerResponse;
import com.customer.project.manager.service.CustomerService;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("CustomerResourceImpl Tests")
class CustomerResourceImplTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerResourceImpl customerResource;

    private MockMvc mockMvc;

    private CustomerResponse customerResponse;
    private UUID customerId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(customerResource).build();

        customerId = UUID.randomUUID();

        customerResponse = new CustomerResponse()
                .setId(customerId)
                .setName("John Doe")
                .setEmail("john.doe@example.com")
                .setPhone("+1234567890");
    }

    @Test
    @DisplayName("Should create customer and return created customer response")
    void testCreateCustomer() throws Exception {
        when(customerService.create(any(CustomerRequest.class))).thenReturn(customerResponse);

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "John Doe",
                                  "password": "password123",
                                  "email": "john.doe@example.com",
                                  "phone": "+1234567890"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(customerResponse.getId().toString())))
                .andExpect(jsonPath("$.name", is(customerResponse.getName())))
                .andExpect(jsonPath("$.email", is(customerResponse.getEmail())))
                .andExpect(jsonPath("$.phone", is(customerResponse.getPhone())));

        verify(customerService).create(any(CustomerRequest.class));
    }

    @Test
    @DisplayName("Should update customer and return updated customer response")
    void testUpdateCustomer() throws Exception {
        when(customerService.update(any(UUID.class), any(CustomerRequest.class))).thenReturn(customerResponse);

        mockMvc.perform(put("/customers/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "John Doe",
                                  "password": "password123",
                                  "email": "john.doe@example.com",
                                  "phone": "+1234567890"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(customerResponse.getId().toString())))
                .andExpect(jsonPath("$.name", is(customerResponse.getName())))
                .andExpect(jsonPath("$.email", is(customerResponse.getEmail())))
                .andExpect(jsonPath("$.phone", is(customerResponse.getPhone())));

        verify(customerService).update(any(UUID.class), any(CustomerRequest.class));
    }

    @Test
    @DisplayName("Should find customer by ID and return customer response")
    void testFindCustomerById() throws Exception {
        when(customerService.findById(any(UUID.class))).thenReturn(customerResponse);

        mockMvc.perform(get("/customers/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(customerResponse.getId().toString())))
                .andExpect(jsonPath("$.name", is(customerResponse.getName())))
                .andExpect(jsonPath("$.email", is(customerResponse.getEmail())))
                .andExpect(jsonPath("$.phone", is(customerResponse.getPhone())))
                .andDo(print());

        verify(customerService).findById(any(UUID.class));
    }

    @Test
    @DisplayName("Should delete customer and return no content")
    void testDeleteCustomer() throws Exception {
        doNothing().when(customerService).delete(any(UUID.class));

        mockMvc.perform(delete("/customers/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(customerService).delete(any(UUID.class));
    }
}
