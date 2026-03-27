package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.services.CurvePointService;
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
class CurveControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockitoBean
    private CurvePointService curvePointService;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    void home_whenAuthenticated_shouldReturnListView() throws Exception {
        Page<CurvePoint> page = new PageImpl<>(List.of(new CurvePoint(1, 1.0, 2.0)));
        when(curvePointService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/curvePoint/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/list"))
                .andExpect(model().attributeExists("curvePoints"));
    }

    @Test
    @WithAnonymousUser
    void home_whenNotAuthenticated_shouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/curvePoint/list"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void addForm_whenAuthenticated_shouldReturnAddView() throws Exception {
        mockMvc.perform(get("/curvePoint/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"));
    }

    @Test
    void validate_whenValidInput_shouldRedirectToList() throws Exception {
        mockMvc.perform(post("/curvePoint/validate")
                        .with(csrf())
                        .param("curveId", "1")
                        .param("term", "1.0")
                        .param("value", "2.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));
    }

    @Test
    void validate_whenInvalidInput_shouldReturnAddView() throws Exception {
        // curveId absent → @NotNull violated
        mockMvc.perform(post("/curvePoint/validate")
                        .with(csrf())
                        .param("term", "1.0"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"));
    }

    @Test
    void showUpdateForm_whenIdExists_shouldReturnUpdateView() throws Exception {
        when(curvePointService.findById(1)).thenReturn(new CurvePoint(1, 1.0, 2.0));

        mockMvc.perform(get("/curvePoint/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"))
                .andExpect(model().attributeExists("curvePoint"));
    }

    @Test
    void showUpdateForm_whenIdNotFound_shouldReturnErrorView() throws Exception {
        when(curvePointService.findById(99)).thenThrow(new ResourceNotFoundException("CurvePoint not found for id: 99"));

        mockMvc.perform(get("/curvePoint/update/99"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error/error"));
    }

    @Test
    void updateCurvePoint_whenValidInput_shouldRedirectToList() throws Exception {
        when(curvePointService.update(eq(1), any())).thenReturn(new CurvePoint(1, 1.0, 2.0));

        mockMvc.perform(post("/curvePoint/update/1")
                        .with(csrf())
                        .param("curveId", "1")
                        .param("term", "1.0")
                        .param("value", "2.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));
    }

    @Test
    void updateCurvePoint_whenInvalidInput_shouldReturnUpdateView() throws Exception {
        mockMvc.perform(post("/curvePoint/update/1")
                        .with(csrf())
                        .param("term", "1.0"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"));
    }

    @Test
    void updateCurvePoint_whenIdNotFound_shouldReturnErrorView() throws Exception {
        when(curvePointService.update(eq(99), any())).thenThrow(new ResourceNotFoundException("CurvePoint not found for id: 99"));

        mockMvc.perform(post("/curvePoint/update/99")
                        .with(csrf())
                        .param("curveId", "1")
                        .param("term", "1.0")
                        .param("value", "2.0"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error/error"));
    }

    @Test
    void deleteCurvePoint_whenAuthenticated_shouldRedirectToList() throws Exception {
        mockMvc.perform(post("/curvePoint/delete/1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));
    }

    @Test
    void deleteCurvePoint_whenIdNotFound_shouldReturnErrorView() throws Exception {
        org.mockito.Mockito.doThrow(new ResourceNotFoundException("CurvePoint not found for id: 99"))
                .when(curvePointService).delete(99);

        mockMvc.perform(post("/curvePoint/delete/99")
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error/error"));
    }
}
