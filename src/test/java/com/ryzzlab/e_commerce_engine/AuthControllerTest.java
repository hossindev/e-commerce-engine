package com.ryzzlab.e_commerce_engine;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void registerOwner_returnsTokenOnSuccess() throws Exception {
        mockMvc.perform(post("/api/auth/owner/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\",\"password\":\"123456\",\"fullName\":\"Test User\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void registerOwner_duplicateEmail_returnsConflict() throws Exception {
        String body = "{\"email\":\"dup@example.com\",\"password\":\"123456\",\"fullName\":\"Test User\"}";

        // First registration succeeds
        mockMvc.perform(post("/api/auth/owner/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());

        // Second registration with same email fails
        mockMvc.perform(post("/api/auth/owner/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict());
    }

    @Test
    void loginOwner_wrongPassword_returnsUnauthorized() throws Exception {
        String registerBody = "{\"email\":\"login@example.com\",\"password\":\"correctpass\",\"fullName\":\"Test User\"}";
        mockMvc.perform(post("/api/auth/owner/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerBody))
                .andExpect(status().isOk());

        String loginBody = "{\"email\":\"login@example.com\",\"password\":\"wrongpass\"}";
        mockMvc.perform(post("/api/auth/owner/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody))
                .andExpect(status().isUnauthorized());
    }
}