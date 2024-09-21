package com.customer.project.manager.resource.impl;

import com.customer.project.manager.payload.security.SignInRequest;
import com.customer.project.manager.payload.security.SignInResponse;
import com.customer.project.manager.resource.SecurityManagerResource;
import com.customer.project.manager.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/security-manager")
@RequiredArgsConstructor
public class SecurityManagerResourceImpl implements SecurityManagerResource {

    private final SecurityService securityService;

    @Override
    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponse> signIn(SignInRequest request) {
        SignInResponse response = securityService.signIn(request);
        return ResponseEntity.ok(response);
    }

}
