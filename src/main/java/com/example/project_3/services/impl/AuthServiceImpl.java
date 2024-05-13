package com.example.project_3.services.impl;

import com.example.project_3.models.ThanhVien;
import com.example.project_3.models.ThongTinSD;
import com.example.project_3.payloads.responses.ThanhVienResponse;
import com.example.project_3.repositories.ForgotPassReponsitory;
import com.example.project_3.repositories.ThanhVienRepository;
import com.example.project_3.repositories.ThongTinSDRepository;
import com.example.project_3.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private ThanhVienRepository thanhVienRepository;

    @Autowired
    private ForgotPassReponsitory forgotPassReponsitory;

    @Autowired
    private ThongTinSDRepository thongTinSDRepository;

    @Override
    public ThanhVien register(ThanhVien tv) {
        return thanhVienRepository.save(tv);
    }

    @Override
    public ThanhVienResponse login(String email, String password) {
        ThanhVien tv = thanhVienRepository.findByEmail(email).orElse(null);
        if (tv == null) {
            return null;
        }

        if (!tv.getPassword().equals(password)) {
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

        ThongTinSD ttsd = new ThongTinSD(0, tv, null, Instant.now(), null, null, null,null);

        thongTinSDRepository.save(ttsd);

        return tvResponse;
    }

    @Override
    public ThanhVienResponse adminLogin(String email, String password) {
        ThanhVien tv = thanhVienRepository.findByEmail(email).orElse(null);
        if (tv == null) {
            return null;
        }

        if (tv.getMaTV() != 0L) {
            return null;
        }

        if (!tv.getPassword().equals(password)) {
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
    public void updateResetPasswordToken(String token, String email) throws Exception {
        ThanhVien customer = forgotPassReponsitory.findByEmail(email);
        if (customer != null) {
            customer.setResetPasswordToken(token);
            forgotPassReponsitory.save(customer);
        } else {
            throw new Exception("Could not find any customer with the email " + email);
        }
    }

    @Override
    public ThanhVien getByResetPasswordToken(String token) {
        return forgotPassReponsitory.findByResetPasswordToken(token);
    }

    @Override
    public void updatePassword(ThanhVien tv, String newPassword) {
        tv.setPassword(newPassword);

        tv.setResetPasswordToken(null);
        forgotPassReponsitory.save(tv);
    }
}
