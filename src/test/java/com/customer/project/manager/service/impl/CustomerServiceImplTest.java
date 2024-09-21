package com.customer.project.manager.service.impl;

import com.customer.project.manager.domain.Customer;
import com.customer.project.manager.exception.EntityNotFoundException;
import com.customer.project.manager.payload.customer.CustomerRequest;
import com.customer.project.manager.payload.customer.CustomerResponse;
import com.customer.project.manager.repository.CustomerRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("CustomerServiceImpl Tests")
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private CustomerRequest customerRequest;
    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customerRequest = new CustomerRequest();
        customerRequest.setName("John Doe");
        customerRequest.setEmail("john.doe@example.com");
        customerRequest.setPhone("123456789");
        customerRequest.setPassword("password");

        customer = Customer.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .email("john.doe@example.com")
                .phone("123456789")
                .password("encodedPassword")
                .build();
    }

    @Test
    @DisplayName("Should create and return customer response")
    void testCreateCustomer() {
        when(passwordEncoder.encode(customerRequest.getPassword())).thenReturn("encodedPassword");
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerResponse response = customerService.create(customerRequest);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(customerRequest.getName());
        assertThat(response.getEmail()).isEqualTo(customerRequest.getEmail());
        assertThat(response.getPhone()).isEqualTo(customerRequest.getPhone());

        ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save(customerCaptor.capture());
        assertThat(customerCaptor.getValue().getPassword()).isEqualTo("encodedPassword");
    }

    @Test
    @DisplayName("Should update and return customer response")
    void testUpdateCustomer() {
        UUID customerId = customer.getId();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerRequest updateRequest = new CustomerRequest();
        updateRequest.setName("Jane Doe");
        updateRequest.setEmail("jane.doe@example.com");
        updateRequest.setPhone("987654321");

        CustomerResponse response = customerService.update(customerId, updateRequest);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(updateRequest.getName());
        assertThat(response.getEmail()).isEqualTo(updateRequest.getEmail());
        assertThat(response.getPhone()).isEqualTo(updateRequest.getPhone());

        verify(customerRepository).findById(customerId);
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when customer not found on update")
    void testUpdateCustomerNotFound() {
        UUID customerId = UUID.randomUUID();
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        CustomerRequest updateRequest = new CustomerRequest();

        assertThatThrownBy(() -> customerService.update(customerId, updateRequest))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Customer not found with id: " + customerId);

        verify(customerRepository).findById(customerId);
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    @DisplayName("Should find and return customer by ID")
    void testFindById() {
        UUID customerId = customer.getId();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        CustomerResponse response = customerService.findById(customerId);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(customerId);

        verify(customerRepository).findById(customerId);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when customer not found by ID")
    void testFindByIdNotFound() {
        UUID customerId = UUID.randomUUID();
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.findById(customerId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Customer not found with id: " + customerId);

        verify(customerRepository).findById(customerId);
    }

    @Test
    @DisplayName("Should return paginated customers")
    void testFindAll() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Customer> customerPage = new PageImpl<>(List.of(customer));
        when(customerRepository.findAll(pageable)).thenReturn(customerPage);

        Page<CustomerResponse> responsePage = customerService.findAll(pageable);

        assertThat(responsePage).isNotEmpty();
        assertThat(responsePage.getContent()).hasSize(1);
        assertThat(responsePage.getContent().get(0).getName()).isEqualTo(customer.getName());

        verify(customerRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Should delete the customer")
    void testDeleteCustomer() {
        UUID customerId = customer.getId();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        customerService.delete(customerId);

        verify(customerRepository).findById(customerId);
        verify(customerRepository).delete(customer);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when customer not found on delete")
    void testDeleteCustomerNotFound() {
        UUID customerId = UUID.randomUUID();
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.delete(customerId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Customer not found with id: " + customerId);

        verify(customerRepository).findById(customerId);
        verify(customerRepository, never()).delete(any(Customer.class));
    }
}
