package com.nnk.springboot.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class RatingValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void rating_whenValid_shouldHaveNoViolations() {
        Rating rating = new Rating("Aaa", "AAA", "AAA", 1);
        assertThat(validator.validate(rating)).isEmpty();
    }

    @Test
    void rating_whenOrderNumberIsNull_shouldFailValidation() {
        Rating rating = new Rating("Aaa", "AAA", "AAA", null);
        Set<ConstraintViolation<Rating>> violations = validator.validate(rating);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("orderNumber"));
    }

    @Test
    void rating_whenOrderNumberIsZero_shouldFailValidation() {
        Rating rating = new Rating("Aaa", "AAA", "AAA", 0);
        Set<ConstraintViolation<Rating>> violations = validator.validate(rating);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("orderNumber"));
    }

    @Test
    void rating_whenOrderNumberIsNegative_shouldFailValidation() {
        Rating rating = new Rating("Aaa", "AAA", "AAA", -1);
        Set<ConstraintViolation<Rating>> violations = validator.validate(rating);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("orderNumber"));
    }
}
