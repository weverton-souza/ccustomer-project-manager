package com.customer.project.manager.service.impl;

import com.customer.project.manager.domain.Customer;
import com.customer.project.manager.exception.AuthenticationException;
import com.customer.project.manager.payload.security.SignInRequest;
import com.customer.project.manager.payload.security.SignInResponse;
import com.customer.project.manager.property.SecurityProperties;
import com.customer.project.manager.repository.CustomerRepository;
import com.customer.project.manager.service.JWTService;
import com.customer.project.manager.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private static final String BEARER = "Bearer";
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityServiceImpl.class);

    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final CustomerRepository customerRepository;
    private final SecurityProperties securityProperties;

    @Override
    public SignInResponse signIn(SignInRequest request) {
        LOGGER.info("class=SecurityService, method=signIn, message=Signing in customer with email: {}", request.getEmail());

        Customer customer = this.customerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    LOGGER.error("class=SecurityService, method=signIn, message=User not found with email: {}", request.getEmail());
                    return new AuthenticationException("User not found");
                });

        LOGGER.debug("class=SecurityService, method=signIn, message=User retrieved from database: {}", customer);

        boolean hasMatch = this.passwordEncoder.matches(request.getPassword(), customer.getPassword());

        LOGGER.info("class=SecurityService, method=signIn, message=User with email: {} has match: {}", request.getEmail(), hasMatch);

        if (hasMatch) {
            LOGGER.info("class=SecurityService, method=signIn, message=User with email: {} signed in successfully", request.getEmail());

            String accessToken = this.jwtService.generateToken(customer);
            LOGGER.debug("class=SecurityService, method=signIn, message=Access token generated for customer: {}", request.getEmail());

            return buildSignInResponse(accessToken);
        } else {
            LOGGER.error("class=SecurityService, method=signIn, message=User with email: {} sign in failed", request.getEmail());
            throw new AuthenticationException("Invalid credentials");
        }
    }

    private SignInResponse buildSignInResponse(String accessToken) {
        LOGGER.info("class=SecurityService, method=buildSignInResponse, message=Token generated successfully");

        return new SignInResponse(
                accessToken,
                this.securityProperties.getTokenExpiration(),
                BEARER
        );
    }
}
