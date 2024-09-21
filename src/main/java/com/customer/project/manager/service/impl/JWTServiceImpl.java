package com.customer.project.manager.service.impl;

import com.customer.project.manager.domain.Customer;
import com.customer.project.manager.property.SecurityProperties;
import com.customer.project.manager.repository.CustomerRepository;
import com.customer.project.manager.service.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JWTServiceImpl implements JWTService {

    private static final String BEARER = "Bearer";
    private static final Logger LOGGER = LoggerFactory.getLogger(JWTServiceImpl.class);

    private final CustomerRepository customerRepository;
    private final SecurityProperties securityProperties;

    @Override
    public String extractUserName(String token) {
        LOGGER.info("class=JWTService, method=extractUserName, message=Extracting username from token");
        return getUsernameFromToken(token);
    }

    @Override
    public String generateToken(Customer customer) {
        LOGGER.info("class=JWTService, method=generateToken, message=Generating token for customer: {}", customer.getEmail());

        Date expirationDate = new Date(System.currentTimeMillis() + securityProperties.getTokenExpiration());

        String token = Jwts.builder()
                .setSubject(customer.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

        LOGGER.info("class=JWTService, method=generateToken, message=Token generated successfully for customer: {}", customer.getEmail());
        return token;
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        LOGGER.info("class=JWTService, method=isTokenValid, message=Checking token validity for user: {}", userDetails.getUsername());
        try {
            return extractUserName(token).equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (Exception ex) {
            LOGGER.error("class=JWTService, method=isTokenValid, message=Token validation failed", ex);
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        LOGGER.info("class=JWTService, method=isTokenExpired, message=Checking if token is expired");
        return getExpirationDateFromToken(token).before(new Date());
    }

    @Override
    public Claims getAllClaimsFromToken(String token) {
        LOGGER.info("class=JWTService, method=getAllClaimsFromToken, message=Extracting all claims from token");
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public String getUsernameFromToken(String token) {
        LOGGER.info("class=JWTService, method=getUsernameFromToken, message=Extracting username from token");
        return getAllClaimsFromToken(token).getSubject();
    }

    @Override
    public Date getExpirationDateFromToken(String token) {
        LOGGER.info("class=JWTService, method=getExpirationDateFromToken, message=Extracting expiration date from token");
        return getAllClaimsFromToken(token).getExpiration();
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        LOGGER.info("class=JWTService, method=loadUserByUsername, message=Fetching user details for: {}", username);
        return this.customerRepository.findByEmail(username).orElseThrow(() -> {
            LOGGER.error("class=JWTService, method=loadUserByUsername, message=User not found: {}", username);
            return new RuntimeException("User not found");
        });
    }

    private Key getSigningKey() {
        LOGGER.info("class=JWTService, method=getSigningKey, message=Retrieving signing key");
        return Keys.hmacShaKeyFor(this.securityProperties.getJwtSigningKey().getBytes());
    }
}
