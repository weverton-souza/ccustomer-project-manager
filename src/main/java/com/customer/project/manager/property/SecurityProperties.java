package com.customer.project.manager.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@AllArgsConstructor
@ConfigurationProperties(prefix = "application.customer-project-manager.security")
public class SecurityProperties {
    private final long tokenExpiration;
    private final String jwtSigningKey;
}
