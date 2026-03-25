package com.nnk.springboot.advice;

import com.nnk.springboot.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new FakeController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Controller
    static class FakeController {
        @GetMapping("/test/notfound")
        public String triggerNotFound() {
            throw new ResourceNotFoundException("BidList not found for id: 99");
        }
    }

    @Test
    void whenResourceNotFoundException_shouldReturn404WithErrorView() throws Exception {
        mockMvc.perform(get("/test/notfound"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("errorMessage"));
    }

    @Test
    void whenResourceNotFoundException_shouldExposeMessageInModel() throws Exception {
        mockMvc.perform(get("/test/notfound"))
                .andExpect(status().isNotFound())
                .andExpect(model().attribute("errorMessage", "BidList not found for id: 99"));
    }
}
