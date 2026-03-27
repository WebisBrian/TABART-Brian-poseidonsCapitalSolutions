package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.services.RatingService;
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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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
class RatingControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockitoBean
    private RatingService ratingService;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    void home_whenAuthenticated_shouldReturnListView() throws Exception {
        Page<Rating> page = new PageImpl<>(List.of(new Rating("Aaa", "AAA", "AAA", 1)));
        when(ratingService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/rating/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/list"))
                .andExpect(model().attributeExists("ratings"));
    }

    @Test
    @WithAnonymousUser
    void home_whenNotAuthenticated_shouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/rating/list"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void addForm_whenAuthenticated_shouldReturnAddView() throws Exception {
        mockMvc.perform(get("/rating/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"));
    }

    @Test
    void validate_whenValidInput_shouldRedirectToList() throws Exception {
        mockMvc.perform(post("/rating/validate")
                        .with(csrf())
                        .param("moodysRating", "Aaa")
                        .param("sandPRating", "AAA")
                        .param("fitchRating", "AAA")
                        .param("orderNumber", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));
    }

    @Test
    void validate_whenInvalidInput_shouldReturnAddView() throws Exception {
        // orderNumber absent → @NotNull violated
        mockMvc.perform(post("/rating/validate")
                        .with(csrf())
                        .param("moodysRating", "Aaa"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"));
    }

    @Test
    void showUpdateForm_whenIdExists_shouldReturnUpdateView() throws Exception {
        when(ratingService.findById(1)).thenReturn(new Rating("Aaa", "AAA", "AAA", 1));

        mockMvc.perform(get("/rating/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/update"))
                .andExpect(model().attributeExists("rating"));
    }

    @Test
    void showUpdateForm_whenIdNotFound_shouldReturnErrorView() throws Exception {
        when(ratingService.findById(99)).thenThrow(new ResourceNotFoundException("Rating not found for id: 99"));

        mockMvc.perform(get("/rating/update/99"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error/error"));
    }

    @Test
    void updateRating_whenValidInput_shouldRedirectToList() throws Exception {
        when(ratingService.update(eq(1), any())).thenReturn(new Rating("Aaa", "AAA", "AAA", 1));

        mockMvc.perform(post("/rating/update/1")
                        .with(csrf())
                        .param("moodysRating", "Aaa")
                        .param("sandPRating", "AAA")
                        .param("fitchRating", "AAA")
                        .param("orderNumber", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));
    }

    @Test
    void updateRating_whenInvalidInput_shouldReturnUpdateView() throws Exception {
        mockMvc.perform(post("/rating/update/1")
                        .with(csrf())
                        .param("moodysRating", "Aaa"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/update"));
    }

    @Test
    void updateRating_whenIdNotFound_shouldReturnErrorView() throws Exception {
        when(ratingService.update(eq(99), any())).thenThrow(new ResourceNotFoundException("Rating not found for id: 99"));

        mockMvc.perform(post("/rating/update/99")
                        .with(csrf())
                        .param("moodysRating", "Aaa")
                        .param("sandPRating", "AAA")
                        .param("fitchRating", "AAA")
                        .param("orderNumber", "1"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error/error"));
    }

    @Test
    void deleteRating_whenAuthenticated_shouldRedirectToList() throws Exception {
        mockMvc.perform(post("/rating/delete/1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));
    }

    @Test
    void deleteRating_whenIdNotFound_shouldReturnErrorView() throws Exception {
        org.mockito.Mockito.doThrow(new ResourceNotFoundException("Rating not found for id: 99"))
                .when(ratingService).delete(99);

        mockMvc.perform(post("/rating/delete/99")
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error/error"));
    }
}
