package com.example.project_3.validators;

import com.example.project_3.payloads.requests.RegisterRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MatchPasswordValidator implements ConstraintValidator<MatchPassword, RegisterRequest> {
    @Override
    public void initialize(MatchPassword constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(RegisterRequest registerRequest, ConstraintValidatorContext context) {
        if (registerRequest == null) {
            return true; // Đã có các ràng buộc khác kiểm tra null
        }

        boolean isValid = registerRequest.getMatKhau() != null && registerRequest.getMatKhau().equals(registerRequest.getXacNhanMatKhau());

        if (!isValid) {
            context.buildConstraintViolationWithTemplate("Xác nhận mật khẩu phải khớp với mật khẩu.")
                    .addPropertyNode("xacNhanMatKhau")
                    .addConstraintViolation();
        }

        return isValid;
    }
}
