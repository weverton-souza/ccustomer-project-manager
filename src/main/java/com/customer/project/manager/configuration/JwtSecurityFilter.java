package com.customer.project.manager.configuration;

import com.customer.project.manager.service.JWTService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@AllArgsConstructor
public class JwtSecurityFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtSecurityFilter.class);

    private final JWTService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestHeader = request.getHeader("Authorization");

        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            String token = requestHeader.substring(7);

            try {
                String userName = this.jwtService.extractUserName(token);

                if (SecurityContextHolder.getContext().getAuthentication() == null && userName != null) {
                    UserDetails user = this.jwtService.loadUserByUsername(userName);

                    if (this.jwtService.isTokenValid(token, user)) {
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    } else {
                        LOGGER.warn("class=JwtSecurityFilter, method=doFilterInternal, message=Token is invalid for user: {}", userName);
                    }
                }
            } catch (ExpiredJwtException e) {
                LOGGER.error("class=JwtSecurityFilter, method=doFilterInternal, message=Token expired: {}", e.getMessage());
            } catch (JwtException e) {
                LOGGER.error("class=JwtSecurityFilter, method=doFilterInternal, message=JWT error: {}", e.getMessage());
            }
        } else {
            LOGGER.warn("class=JwtSecurityFilter, method=doFilterInternal, message=No Authorization header or Bearer token not found");
        }
        filterChain.doFilter(request, response);
    }
}
