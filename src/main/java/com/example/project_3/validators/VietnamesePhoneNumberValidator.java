package com.example.project_3.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class VietnamesePhoneNumberValidator implements ConstraintValidator<VietnamesePhoneNumber, String> {
    @Override
    public void initialize(VietnamesePhoneNumber constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext constraintValidatorContext) {
        return phone != null && phone.matches("^(\\+?84|0)[35789]\\d{8}$");
    }
}
