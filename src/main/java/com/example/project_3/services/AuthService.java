package com.example.project_3.services;

import com.example.project_3.models.ThanhVien;
import com.example.project_3.payloads.responses.ThanhVienResponse;

public interface AuthService {
    ThanhVien register(ThanhVien tv);
    ThanhVienResponse login(String email, String password);
    ThanhVienResponse adminLogin(String email, String password);

    void updateResetPasswordToken(String token, String email) throws Exception;

    void updatePassword(ThanhVien tv, String newPassword);

    ThanhVien getByResetPasswordToken(String token);
}
