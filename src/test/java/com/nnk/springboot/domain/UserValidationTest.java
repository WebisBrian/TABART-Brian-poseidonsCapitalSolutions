package com.nnk.springboot.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void user_whenValid_shouldHaveNoViolations() {
        User user = new User("john", "Password1!", "John Doe", "USER");
        assertThat(validator.validate(user)).isEmpty();
    }

    // --- username ---

    @Test
    void user_whenUsernameIsBlank_shouldFailValidation() {
        User user = new User("", "Password1!", "John Doe", "USER");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("username"));
    }

    // --- fullname ---

    @Test
    void user_whenFullnameIsBlank_shouldFailValidation() {
        User user = new User("john", "Password1!", "", "USER");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("fullname"));
    }

    // --- role ---

    @Test
    void user_whenRoleIsBlank_shouldFailValidation() {
        User user = new User("john", "Password1!", "John Doe", "");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("role"));
    }

    // --- password regex ---

    @Test
    void user_whenPasswordHasNoUppercase_shouldFailValidation() {
        User user = new User("john", "password1!", "John Doe", "USER");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("password"));
    }

    @Test
    void user_whenPasswordHasNoDigit_shouldFailValidation() {
        User user = new User("john", "Password!!", "John Doe", "USER");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("password"));
    }

    @Test
    void user_whenPasswordHasNoSpecialChar_shouldFailValidation() {
        User user = new User("john", "Password1A", "John Doe", "USER");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("password"));
    }

    @Test
    void user_whenPasswordIsTooShort_shouldFailValidation() {
        User user = new User("john", "Pa1!", "John Doe", "USER");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("password"));
    }
}
