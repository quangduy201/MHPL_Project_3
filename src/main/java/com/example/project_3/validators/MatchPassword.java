package com.example.project_3.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MatchPasswordValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MatchPassword {
    String message() default "Xác nhận mật khẩu phải khớp với mật khẩu.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
