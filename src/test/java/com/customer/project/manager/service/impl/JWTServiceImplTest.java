package com.customer.project.manager.service.impl;

import com.customer.project.manager.domain.Customer;
import com.customer.project.manager.property.SecurityProperties;
import com.customer.project.manager.repository.CustomerRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("JWTServiceImpl Tests")
class JWTServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private SecurityProperties securityProperties;

    @InjectMocks
    private JWTServiceImpl jwtService;

    private Customer customer;
    private Key signingKey;
    private String token;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customer = Customer.builder()
                .id(UUID.randomUUID())
                .email("john.doe@example.com")
                .password("password")
                .build();

        when(securityProperties.getTokenExpiration()).thenReturn(100000L);
        when(securityProperties.getJwtSigningKey()).thenReturn("mySuperSecretKeyForJwtSignatures");

        signingKey = Keys.hmacShaKeyFor(securityProperties.getJwtSigningKey().getBytes());

        token = Jwts.builder()
                .setSubject(customer.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + securityProperties.getTokenExpiration()))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    @Test
    @DisplayName("Should extract username from token")
    void testExtractUserName() {
        String username = jwtService.extractUserName(token);
        assertThat(username).isEqualTo(customer.getEmail());
    }

    @Test
    @DisplayName("Should generate a valid token for customer")
    void testGenerateToken() {
        String generatedToken = jwtService.generateToken(customer);

        assertThat(generatedToken).isNotNull();
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(generatedToken)
                .getBody();

        assertThat(claims.getSubject()).isEqualTo(customer.getEmail());
        assertThat(claims.getExpiration()).isAfter(new Date());
    }

    @Test
    @DisplayName("Should return true for valid token")
    void testIsTokenValid() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(customer.getEmail());

        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("Should return false for invalid username")
    void testIsTokenValidWithInvalidUsername() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("invalid@example.com");

        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Should return false for expired token")
    void testIsTokenValidWithExpiredToken() {
        // Generate an expired token
        String expiredToken = Jwts.builder()
                .setSubject(customer.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis() - 100000))
                .setExpiration(new Date(System.currentTimeMillis() - 1000))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(customer.getEmail());

        boolean isValid = jwtService.isTokenValid(expiredToken, userDetails);

        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Should extract claims from token")
    void testGetAllClaimsFromToken() {
        Claims claims = jwtService.getAllClaimsFromToken(token);

        assertThat(claims).isNotNull();
        assertThat(claims.getSubject()).isEqualTo(customer.getEmail());
    }

    @Test
    @DisplayName("Should extract username from token")
    void testGetUsernameFromToken() {
        String username = jwtService.getUsernameFromToken(token);

        assertThat(username).isEqualTo(customer.getEmail());
    }

    @Test
    @DisplayName("Should extract expiration date from token")
    void testGetExpirationDateFromToken() {
        Date expirationDate = jwtService.getExpirationDateFromToken(token);

        assertThat(expirationDate).isAfter(new Date());
    }

    @Test
    @DisplayName("Should return user details for valid username")
    void testLoadUserByUsername() {
        when(customerRepository.findByEmail(customer.getEmail())).thenReturn(Optional.of(customer));

        UserDetails userDetails = jwtService.loadUserByUsername(customer.getEmail());

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(customer.getEmail());
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void testLoadUserByUsernameNotFound() {
        when(customerRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> jwtService.loadUserByUsername("notfound@example.com"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");
    }
}
