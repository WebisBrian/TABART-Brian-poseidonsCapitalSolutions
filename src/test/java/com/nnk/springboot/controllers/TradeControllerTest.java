package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.services.TradeService;
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
class TradeControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockitoBean
    private TradeService tradeService;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    void home_whenAuthenticated_shouldReturnListView() throws Exception {
        when(tradeService.findAll()).thenReturn(List.of(new Trade("acc", "type", 10.0)));

        mockMvc.perform(get("/trade/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/list"))
                .andExpect(model().attributeExists("trades"));
    }

    @Test
    @WithAnonymousUser
    void home_whenNotAuthenticated_shouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/trade/list"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void addForm_whenAuthenticated_shouldReturnAddView() throws Exception {
        mockMvc.perform(get("/trade/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"));
    }

    @Test
    void validate_whenValidInput_shouldRedirectToList() throws Exception {
        mockMvc.perform(post("/trade/validate")
                        .with(csrf())
                        .param("account", "testAccount")
                        .param("type", "testType")
                        .param("buyQuantity", "10.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));
    }

    @Test
    void validate_whenInvalidInput_shouldReturnAddView() throws Exception {
        // account blank → @NotBlank violated
        mockMvc.perform(post("/trade/validate")
                        .with(csrf())
                        .param("account", "")
                        .param("type", "testType"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"));
    }

    @Test
    void showUpdateForm_whenIdExists_shouldReturnUpdateView() throws Exception {
        when(tradeService.findById(1)).thenReturn(new Trade("acc", "type", 10.0));

        mockMvc.perform(get("/trade/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"))
                .andExpect(model().attributeExists("trade"));
    }

    @Test
    void showUpdateForm_whenIdNotFound_shouldReturnErrorView() throws Exception {
        when(tradeService.findById(99)).thenThrow(new ResourceNotFoundException("Trade not found for id: 99"));

        mockMvc.perform(get("/trade/update/99"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"));
    }

    @Test
    void updateTrade_whenValidInput_shouldRedirectToList() throws Exception {
        when(tradeService.update(eq(1), any())).thenReturn(new Trade("acc", "type", 10.0));

        mockMvc.perform(post("/trade/update/1")
                        .with(csrf())
                        .param("account", "testAccount")
                        .param("type", "testType")
                        .param("buyQuantity", "10.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));
    }

    @Test
    void updateTrade_whenInvalidInput_shouldReturnUpdateView() throws Exception {
        mockMvc.perform(post("/trade/update/1")
                        .with(csrf())
                        .param("account", "")
                        .param("type", "testType"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"));
    }

    @Test
    void deleteTrade_whenAuthenticated_shouldRedirectToList() throws Exception {
        mockMvc.perform(post("/trade/delete/1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));
    }
}
