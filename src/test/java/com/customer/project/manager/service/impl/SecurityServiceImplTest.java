package com.customer.project.manager.service.impl;

import com.customer.project.manager.domain.Customer;
import com.customer.project.manager.exception.AuthenticationException;
import com.customer.project.manager.payload.security.SignInRequest;
import com.customer.project.manager.payload.security.SignInResponse;
import com.customer.project.manager.property.SecurityProperties;
import com.customer.project.manager.repository.CustomerRepository;
import com.customer.project.manager.service.JWTService;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("SecurityServiceImpl Tests")
class SecurityServiceImplTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JWTService jwtService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private SecurityProperties securityProperties;

    @InjectMocks
    private SecurityServiceImpl securityService;

    private SignInRequest signInRequest;
    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        signInRequest = new SignInRequest();
        signInRequest.setEmail("john.doe@example.com");
        signInRequest.setPassword("password");

        customer = Customer.builder()
                .id(UUID.randomUUID())
                .email("john.doe@example.com")
                .password("encodedPassword")
                .build();

        when(securityProperties.getTokenExpiration()).thenReturn(100000L);
    }

    @Test
    @DisplayName("Should return SignInResponse for valid credentials")
    void testSignInSuccess() {
        when(customerRepository.findByEmail(signInRequest.getEmail())).thenReturn(Optional.of(customer));
        when(passwordEncoder.matches(signInRequest.getPassword(), customer.getPassword())).thenReturn(true);
        when(jwtService.generateToken(customer)).thenReturn("generatedToken");

        SignInResponse response = securityService.signIn(signInRequest);

        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isEqualTo("generatedToken");
        assertThat(response.getTokenType()).isEqualTo("Bearer");
        assertThat(response.getExpiration()).isEqualTo(securityProperties.getTokenExpiration());

        verify(customerRepository).findByEmail(signInRequest.getEmail());
        verify(passwordEncoder).matches(signInRequest.getPassword(), customer.getPassword());
        verify(jwtService).generateToken(customer);
    }

    @Test
    @DisplayName("Should throw AuthenticationException when user not found")
    void testSignInUserNotFound() {
        when(customerRepository.findByEmail(signInRequest.getEmail())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> securityService.signIn(signInRequest))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("User not found");

        verify(customerRepository).findByEmail(signInRequest.getEmail());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtService, never()).generateToken(any(Customer.class));
    }

    @Test
    @DisplayName("Should throw AuthenticationException when password does not match")
    void testSignInInvalidPassword() {
        when(customerRepository.findByEmail(signInRequest.getEmail())).thenReturn(Optional.of(customer));
        when(passwordEncoder.matches(signInRequest.getPassword(), customer.getPassword())).thenReturn(false);

        assertThatThrownBy(() -> securityService.signIn(signInRequest))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("Invalid credentials");

        verify(customerRepository).findByEmail(signInRequest.getEmail());
        verify(passwordEncoder).matches(signInRequest.getPassword(), customer.getPassword());
        verify(jwtService, never()).generateToken(any(Customer.class));
    }
}
