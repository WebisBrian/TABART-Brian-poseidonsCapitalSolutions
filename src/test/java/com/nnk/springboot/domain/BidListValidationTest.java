package com.nnk.springboot.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class BidListValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void bidList_whenValid_shouldHaveNoViolations() {
        BidList bid = new BidList("Account", "Type", 10.0);
        assertThat(validator.validate(bid)).isEmpty();
    }

    @Test
    void bidList_whenAccountIsBlank_shouldFailValidation() {
        BidList bid = new BidList("", "Type", 10.0);
        Set<ConstraintViolation<BidList>> violations = validator.validate(bid);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("account"));
    }

    @Test
    void bidList_whenTypeIsBlank_shouldFailValidation() {
        BidList bid = new BidList("Account", "", 10.0);
        Set<ConstraintViolation<BidList>> violations = validator.validate(bid);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("type"));
    }

    @Test
    void bidList_whenBidQuantityIsNegative_shouldFailValidation() {
        BidList bid = new BidList("Account", "Type", -1.0);
        Set<ConstraintViolation<BidList>> violations = validator.validate(bid);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("bidQuantity"));
    }

    @Test
    void bidList_whenBidQuantityIsZero_shouldBeValid() {
        BidList bid = new BidList("Account", "Type", 0.0);
        assertThat(validator.validate(bid)).isEmpty();
    }

    // --- @Size ---

    @Test
    void bidList_whenAccountTooLong_shouldFailValidation() {
        BidList bid = new BidList("A".repeat(31), "Type", 10.0);
        Set<ConstraintViolation<BidList>> violations = validator.validate(bid);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("account"));
    }

    @Test
    void bidList_whenTypeTooLong_shouldFailValidation() {
        BidList bid = new BidList("Account", "T".repeat(31), 10.0);
        Set<ConstraintViolation<BidList>> violations = validator.validate(bid);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("type"));
    }

    @Test
    void bidList_whenStatusTooLong_shouldFailValidation() {
        BidList bid = new BidList("Account", "Type", 10.0);
        bid.setStatus("S".repeat(11));
        Set<ConstraintViolation<BidList>> violations = validator.validate(bid);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("status"));
    }
}
