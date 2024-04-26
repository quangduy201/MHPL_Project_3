package com.example.project_3.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = OnlyNumberValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OnlyNumber {
    String message() default "Invalid number";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
