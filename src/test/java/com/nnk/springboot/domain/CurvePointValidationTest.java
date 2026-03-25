package com.nnk.springboot.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class CurvePointValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void curvePoint_whenValid_shouldHaveNoViolations() {
        CurvePoint cp = new CurvePoint(10, 5.0, 100.0);
        assertThat(validator.validate(cp)).isEmpty();
    }

    @Test
    void curvePoint_whenCurveIdIsNull_shouldFailValidation() {
        CurvePoint cp = new CurvePoint(null, 5.0, 100.0);
        Set<ConstraintViolation<CurvePoint>> violations = validator.validate(cp);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("curveId"));
    }

    @Test
    void curvePoint_whenTermIsNegative_shouldFailValidation() {
        CurvePoint cp = new CurvePoint(10, -1.0, 100.0);
        Set<ConstraintViolation<CurvePoint>> violations = validator.validate(cp);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("term"));
    }

    @Test
    void curvePoint_whenTermIsZero_shouldBeValid() {
        CurvePoint cp = new CurvePoint(10, 0.0, 100.0);
        assertThat(validator.validate(cp)).isEmpty();
    }
}
