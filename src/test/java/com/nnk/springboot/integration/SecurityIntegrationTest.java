package com.nnk.springboot.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class SecurityIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    // --- Page de login ---

    @Test
    @WithMockUser
    void authenticatedUser_accessLoginPage_shouldRedirectToHome() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    // --- Accès sans authentification ---

    @Test
    void unauthenticated_accessProtectedRoute_shouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void unauthenticated_accessLoginPage_shouldBeAllowed() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    // --- Accès avec rôle USER ---

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void authenticatedUser_accessBidList_shouldBeAllowed() throws Exception {
        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void authenticatedUser_accessUserManagement_shouldBeForbidden() throws Exception {
        mockMvc.perform(get("/admin/user/list"))
                .andExpect(status().isForbidden());
    }

    // --- Accès avec rôle ADMIN ---

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void authenticatedAdmin_accessUserManagement_shouldBeAllowed() throws Exception {
        mockMvc.perform(get("/admin/user/list"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void authenticatedAdmin_accessBidList_shouldBeAllowed() throws Exception {
        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().isOk());
    }
}
