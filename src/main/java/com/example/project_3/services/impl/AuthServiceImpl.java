package com.example.project_3.services.impl;

import com.example.project_3.models.ThanhVien;
import com.example.project_3.payloads.responses.ThanhVienResponse;
import com.example.project_3.repositories.ThanhVienRepository;
import com.example.project_3.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private ThanhVienRepository thanhVienRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ThanhVien register(ThanhVien tv) {
        String encodedPassword = passwordEncoder.encode(tv.getPassword());
        tv.setPassword(encodedPassword);
        return thanhVienRepository.save(tv);
    }

    @Override
    public ThanhVienResponse login(String email, String password) {
        ThanhVien tv = thanhVienRepository.findByEmail(email);
        if (tv == null) {
            return null;
        }

        if (!passwordEncoder.matches(password, tv.getPassword())) {
            return null;
        }

        ThanhVienResponse tvResponse = new ThanhVienResponse(
                tv.getMaTV(),
                tv.getHoTen(),
                tv.getKhoa(),
                tv.getNganh(),
                tv.getSdt(),
                tv.getEmail()
        );

        return tvResponse;
    }

    @Override
    public void resetPassword(String email, String oldPassword, String newPassword) {

    }
}
