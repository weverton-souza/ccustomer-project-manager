package com.customer.project.manager.service;

import com.customer.project.manager.payload.security.SignInRequest;
import com.customer.project.manager.payload.security.SignInResponse;

public interface SecurityService {
    SignInResponse signIn(SignInRequest signInRequest);
}
