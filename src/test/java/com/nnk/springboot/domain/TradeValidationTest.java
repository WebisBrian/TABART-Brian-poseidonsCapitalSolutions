package com.nnk.springboot.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class TradeValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void trade_whenValid_shouldHaveNoViolations() {
        Trade trade = new Trade("Account", "Type", 10.0);
        assertThat(validator.validate(trade)).isEmpty();
    }

    @Test
    void trade_whenAccountIsBlank_shouldFailValidation() {
        Trade trade = new Trade("", "Type", 10.0);
        Set<ConstraintViolation<Trade>> violations = validator.validate(trade);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("account"));
    }

    @Test
    void trade_whenTypeIsBlank_shouldFailValidation() {
        Trade trade = new Trade("Account", "", 10.0);
        Set<ConstraintViolation<Trade>> violations = validator.validate(trade);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("type"));
    }

    @Test
    void trade_whenBuyQuantityIsNegative_shouldFailValidation() {
        Trade trade = new Trade("Account", "Type", -1.0);
        Set<ConstraintViolation<Trade>> violations = validator.validate(trade);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("buyQuantity"));
    }

    @Test
    void trade_whenBuyQuantityIsZero_shouldBeValid() {
        Trade trade = new Trade("Account", "Type", 0.0);
        assertThat(validator.validate(trade)).isEmpty();
    }

    // --- @Size ---

    @Test
    void trade_whenAccountTooLong_shouldFailValidation() {
        Trade trade = new Trade("A".repeat(31), "Type", 10.0);
        Set<ConstraintViolation<Trade>> violations = validator.validate(trade);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("account"));
    }

    @Test
    void trade_whenTypeTooLong_shouldFailValidation() {
        Trade trade = new Trade("Account", "T".repeat(31), 10.0);
        Set<ConstraintViolation<Trade>> violations = validator.validate(trade);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("type"));
    }

    @Test
    void trade_whenStatusTooLong_shouldFailValidation() {
        Trade trade = new Trade("Account", "Type", 10.0);
        trade.setStatus("S".repeat(11));
        Set<ConstraintViolation<Trade>> violations = validator.validate(trade);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("status"));
    }
}
