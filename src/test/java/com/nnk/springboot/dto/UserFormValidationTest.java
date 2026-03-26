package com.nnk.springboot.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserFormValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void userForm_whenValid_shouldHaveNoViolations() {
        UserForm form = new UserForm(null, "john", "Password1!", "John Doe", "USER");
        assertThat(validator.validate(form)).isEmpty();
    }

    // --- password regex ---

    @Test
    void userForm_whenPasswordHasNoUppercase_shouldFailValidation() {
        UserForm form = new UserForm(null, "john", "password1!", "John Doe", "USER");
        Set<ConstraintViolation<UserForm>> violations = validator.validate(form);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("password"));
    }

    @Test
    void userForm_whenPasswordHasNoDigit_shouldFailValidation() {
        UserForm form = new UserForm(null, "john", "Password!!", "John Doe", "USER");
        Set<ConstraintViolation<UserForm>> violations = validator.validate(form);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("password"));
    }

    @Test
    void userForm_whenPasswordHasNoSpecialChar_shouldFailValidation() {
        UserForm form = new UserForm(null, "john", "Password1A", "John Doe", "USER");
        Set<ConstraintViolation<UserForm>> violations = validator.validate(form);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("password"));
    }

    @Test
    void userForm_whenPasswordIsTooShort_shouldFailValidation() {
        UserForm form = new UserForm(null, "john", "Pa1!", "John Doe", "USER");
        Set<ConstraintViolation<UserForm>> violations = validator.validate(form);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("password"));
    }
}