package com.customer.project.manager.service;

import com.customer.project.manager.domain.Customer;
import io.jsonwebtoken.Claims;
import java.util.Date;
import org.springframework.security.core.userdetails.UserDetails;

public interface JWTService {
    String extractUserName(String token);

    String generateToken(Customer user);

    boolean isTokenValid(String token, UserDetails userDetails);

    Claims getAllClaimsFromToken(String token);

    String getUsernameFromToken(String token);

    Date getExpirationDateFromToken(String token);

    UserDetails loadUserByUsername(String username);
}
