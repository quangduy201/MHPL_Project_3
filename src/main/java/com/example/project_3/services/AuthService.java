package com.example.project_3.services;

import com.example.project_3.models.ThanhVien;
import com.example.project_3.payloads.responses.ThanhVienResponse;

public interface AuthService {
    ThanhVien register(ThanhVien tv);
    ThanhVienResponse login(String email, String password);

    void resetPassword(String email, String oldPassword, String newPassword);
}
