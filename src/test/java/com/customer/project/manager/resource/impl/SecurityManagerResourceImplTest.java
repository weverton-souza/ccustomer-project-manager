package com.customer.project.manager.resource.impl;

import com.customer.project.manager.payload.security.SignInRequest;
import com.customer.project.manager.payload.security.SignInResponse;
import com.customer.project.manager.service.SecurityService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("SecurityManagerResourceImpl Tests")
class SecurityManagerResourceImplTest {

    private final Gson gson = new Gson();

    @Mock
    private SecurityService securityService;

    @InjectMocks
    private SecurityManagerResourceImpl securityManagerResource;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(securityManagerResource).build();
    }

    @Test
    @DisplayName("Should sign in and return sign in response")
    void testSignIn() throws Exception {
        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setEmail("john.doe@example.com");
        signInRequest.setPassword("password123");

        SignInResponse signInResponse = new SignInResponse();
        signInResponse.setAccessToken("mock-token");
        signInResponse.setExpiration(3600L);
        signInResponse.setTokenType("Bearer");

        when(securityService.signIn(any(SignInRequest.class))).thenReturn(signInResponse);

        mockMvc.perform(post("/security-manager/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(signInRequest)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.accessToken").value("mock-token"))
                .andExpect(jsonPath("$.expiration").value(3600))
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }
}
