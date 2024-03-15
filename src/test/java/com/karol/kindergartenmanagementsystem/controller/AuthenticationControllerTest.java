package com.karol.kindergartenmanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.karol.kindergartenmanagementsystem.dto.AuthenticationResponse;
import com.karol.kindergartenmanagementsystem.dto.SignInRequest;
import com.karol.kindergartenmanagementsystem.filter.JwtAuthenticationFilter;
import com.karol.kindergartenmanagementsystem.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthenticationService authenticationService;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenValidSignInRequest_whenLogin_thenReturnsIsOkStatus() throws Exception {
        SignInRequest request = SignInRequest.builder()
                .email("test@example.com")
                .password("password")
                .build();
        AuthenticationResponse response = new AuthenticationResponse("token");
        when(authenticationService.authenticate(request)).thenReturn(response);

        ResultActions responseResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        responseResult.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void givenValidSignInRequest_whenLogin_thenReturnsTokenInResponse() throws Exception {
        SignInRequest request = SignInRequest.builder()
                .email("test@example.com")
                .password("password")
                .build();
        AuthenticationResponse response = new AuthenticationResponse("expectedToken");
        when(authenticationService.authenticate(request)).thenReturn(response);

        ResultActions responseResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        responseResult.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    public void givenInvalidSignInRequest_whenLogin_thenReturnsBadRequest() throws Exception {
        SignInRequest request = SignInRequest.builder()
                .email("testexample")
                .password("password")
                .build();
        AuthenticationResponse response = new AuthenticationResponse("token");
        when(authenticationService.authenticate(request)).thenReturn(response);

        ResultActions responseResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        responseResult.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(notNullValue()));
    }
}