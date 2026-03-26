package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@WithMockUser(roles = "ADMIN")
class UserControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    void home_whenAdmin_shouldReturnListView() throws Exception {
        when(userService.findAll()).thenReturn(List.of(new User("user1", "Pass1!", "Full Name", "USER")));

        mockMvc.perform(get("/admin/user/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/list"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void home_whenUser_shouldBeForbidden() throws Exception {
        mockMvc.perform(get("/admin/user/list"))
                .andExpect(status().isForbidden());
    }

    @Test
    void addForm_whenAdmin_shouldReturnAddView() throws Exception {
        mockMvc.perform(get("/admin/user/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"));
    }

    @Test
    void validate_whenValidInput_shouldRedirectToList() throws Exception {
        mockMvc.perform(post("/admin/user/validate")
                        .with(csrf())
                        .param("username", "testUser")
                        .param("password", "Test1234!")
                        .param("fullname", "Test User")
                        .param("role", "USER"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/user/list"));
    }

    @Test
    void validate_whenUsernameBlank_shouldReturnAddView() throws Exception {
        mockMvc.perform(post("/admin/user/validate")
                        .with(csrf())
                        .param("username", "")
                        .param("password", "Test1234!")
                        .param("fullname", "Test User")
                        .param("role", "USER"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"));
    }

    @Test
    void validate_whenPasswordInvalid_shouldReturnAddView() throws Exception {
        // password without uppercase → @Pattern violated
        mockMvc.perform(post("/admin/user/validate")
                        .with(csrf())
                        .param("username", "testUser")
                        .param("password", "weakpass1!")
                        .param("fullname", "Test User")
                        .param("role", "USER"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"));
    }

    @Test
    void showUpdateForm_whenIdExists_shouldReturnUpdateViewWithClearedPassword() throws Exception {
        when(userService.findById(1)).thenReturn(new User("user1", "HashedPass1!", "Full Name", "USER"));

        mockMvc.perform(get("/admin/user/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"))
                .andExpect(model().attribute("user", hasProperty("password", emptyString())));
    }

    @Test
    void showUpdateForm_whenIdNotFound_shouldReturnErrorView() throws Exception {
        when(userService.findById(99)).thenThrow(new ResourceNotFoundException("User not found for id: 99"));

        mockMvc.perform(get("/admin/user/update/99"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error/error"));
    }

    @Test
    void updateUser_whenValidInput_shouldRedirectToList() throws Exception {
        when(userService.update(eq(1), any())).thenReturn(new User("user1", "Test1234!", "Full Name", "USER"));

        mockMvc.perform(post("/admin/user/update/1")
                        .with(csrf())
                        .param("username", "testUser")
                        .param("password", "Test1234!")
                        .param("fullname", "Test User")
                        .param("role", "USER"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/user/list"));
    }

    @Test
    void updateUser_whenInvalidInput_shouldReturnUpdateView() throws Exception {
        mockMvc.perform(post("/admin/user/update/1")
                        .with(csrf())
                        .param("username", "")
                        .param("password", "Test1234!")
                        .param("fullname", "Test User")
                        .param("role", "USER"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"));
    }

    @Test
    void deleteUser_whenAdmin_shouldRedirectToList() throws Exception {
        mockMvc.perform(post("/admin/user/delete/1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/user/list"));
    }
}
