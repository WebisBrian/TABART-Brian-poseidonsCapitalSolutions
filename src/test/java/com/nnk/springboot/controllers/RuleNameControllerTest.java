package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.services.RuleNameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@WithMockUser
class RuleNameControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockitoBean
    private RuleNameService ruleNameService;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    void home_whenAuthenticated_shouldReturnListView() throws Exception {
        when(ruleNameService.findAll()).thenReturn(List.of(new RuleName("rule1", "desc", null, null, null, null)));

        mockMvc.perform(get("/ruleName/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/list"))
                .andExpect(model().attributeExists("ruleNames"));
    }

    @Test
    @WithAnonymousUser
    void home_whenNotAuthenticated_shouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/ruleName/list"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void addForm_whenAuthenticated_shouldReturnAddView() throws Exception {
        mockMvc.perform(get("/ruleName/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"));
    }

    @Test
    void validate_whenValidInput_shouldRedirectToList() throws Exception {
        mockMvc.perform(post("/ruleName/validate")
                        .with(csrf())
                        .param("name", "testRule")
                        .param("description", "a description"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));
    }

    @Test
    void validate_whenInvalidInput_shouldReturnAddView() throws Exception {
        // name blank → @NotBlank violated
        mockMvc.perform(post("/ruleName/validate")
                        .with(csrf())
                        .param("name", "")
                        .param("description", "a description"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"));
    }

    @Test
    void showUpdateForm_whenIdExists_shouldReturnUpdateView() throws Exception {
        when(ruleNameService.findById(1)).thenReturn(new RuleName("rule1", "desc", null, null, null, null));

        mockMvc.perform(get("/ruleName/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/update"))
                .andExpect(model().attributeExists("ruleName"));
    }

    @Test
    void showUpdateForm_whenIdNotFound_shouldReturnErrorView() throws Exception {
        when(ruleNameService.findById(99)).thenThrow(new ResourceNotFoundException("RuleName not found for id: 99"));

        mockMvc.perform(get("/ruleName/update/99"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"));
    }

    @Test
    void updateRuleName_whenValidInput_shouldRedirectToList() throws Exception {
        when(ruleNameService.update(eq(1), any())).thenReturn(new RuleName("rule1", "desc", null, null, null, null));

        mockMvc.perform(post("/ruleName/update/1")
                        .with(csrf())
                        .param("name", "testRule")
                        .param("description", "a description"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));
    }

    @Test
    void updateRuleName_whenInvalidInput_shouldReturnUpdateView() throws Exception {
        mockMvc.perform(post("/ruleName/update/1")
                        .with(csrf())
                        .param("name", "")
                        .param("description", "a description"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/update"));
    }

    @Test
    void deleteRuleName_whenAuthenticated_shouldRedirectToList() throws Exception {
        mockMvc.perform(get("/ruleName/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));
    }
}
