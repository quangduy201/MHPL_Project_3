package com.example.project_3.controllers;

import com.example.project_3.models.ThanhVien;
import com.example.project_3.payloads.requests.RegisterRequest;
import com.example.project_3.payloads.requests.ThanhVienRequest;
import com.example.project_3.services.RegisterService;
import com.example.project_3.services.ThanhVienService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegisterController {
    @Autowired
    private RegisterService registerService;
    @GetMapping({"/", ""})
    public String getForm(Model model) {
        model.addAttribute("tv", new RegisterRequest());
        return "register/index";
    }


    @PostMapping({"/", ""})
    public String registerSubmit(@Valid @ModelAttribute("tv") RegisterRequest tv, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            System.out.printf(tv.getMatKhau());
            System.out.printf(tv.getXacNhanMatKhau());
            if (!tv.isXacNhanMatKhauValid()) {
                bindingResult.rejectValue("xacNhanMatKhau", "password.mismatch", "Trường này không khớp với trường mật khẩu");
                bindingResult.rejectValue("matKhau", "password.mismatch", "Trường này không khớp với trường xác nhận mật khẩu");
            }
            model.addAttribute("tv", tv);
            return "register/index";
        }

        ThanhVien thanhVien = new ThanhVien();
        thanhVien.setMaTV(Long.valueOf(tv.getMaTV()));
        thanhVien.setHoTen(tv.getHoTen());
        thanhVien.setKhoa(tv.getKhoa());
        thanhVien.setNganh(tv.getNganh());
        thanhVien.setSdt(tv.getSdt());
        thanhVien.setEmail(tv.getEmail());
        thanhVien.setPassword(tv.getMatKhau());

        registerService.register(thanhVien);

        // Logic to save the user to the database
        return "redirect:/user/" + thanhVien.getMaTV();
    }
}
