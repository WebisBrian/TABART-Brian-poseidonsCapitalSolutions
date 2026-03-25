package com.nnk.springboot.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class RuleNameValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void ruleName_whenValid_shouldHaveNoViolations() {
        RuleName rule = new RuleName("Rule Name", "Description", "Json", "Template", "SQL", "SQL Part");
        assertThat(validator.validate(rule)).isEmpty();
    }

    @Test
    void ruleName_whenNameIsBlank_shouldFailValidation() {
        RuleName rule = new RuleName("", "Description", "Json", "Template", "SQL", "SQL Part");
        Set<ConstraintViolation<RuleName>> violations = validator.validate(rule);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }
}
