/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.project_3.services;

import com.example.project_3.models.ThanhVien;
import com.example.project_3.models.XuLy;
import com.example.project_3.repositories.ForgotPassReponsitory;
import com.example.project_3.repositories.XuLyRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author vithi
 */
@Service
@Transactional
public class CustomerServices {
    @Autowired
    private ForgotPassReponsitory customerRepo;
    @Autowired
    private XuLyRepository xuLyRepository;


    public List<XuLy> getXuLyByMaTV(Long maTV) {
        return xuLyRepository.findByMaTV(maTV);
    }

    public void updateResetPasswordToken(String token, String email) throws Exception {
        ThanhVien customer = customerRepo.findByEmail(email);
        if (customer != null) {
            customer.setResetPasswordToken(token);
            customerRepo.save(customer);
        } else {
            throw new Exception("Could not find any customer with the email " + email);
        }
    }
     
    public ThanhVien getByResetPasswordToken(String token) {
        return customerRepo.findByResetPasswordToken(token);
    }
     
    public void updatePassword(ThanhVien customer, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        customer.setPassword(newPassword);

        customer.setResetPasswordToken(null);
        customerRepo.save(customer);
    }
}
