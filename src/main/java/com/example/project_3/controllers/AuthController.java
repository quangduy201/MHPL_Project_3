package com.example.project_3.controllers;

import com.example.project_3.models.ThanhVien;
import com.example.project_3.payloads.requests.LoginRequest;
import com.example.project_3.payloads.requests.RegisterRequest;
import com.example.project_3.payloads.responses.ThanhVienResponse;
import com.example.project_3.services.AuthService;
import com.example.project_3.services.ThanhVienService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private ThanhVienService thanhVienService;

    @GetMapping({"/login", "/login/"})
    public String loginForm(Model model) {

        model.addAttribute("tv", new LoginRequest());

        return "login/index";
    }

    @PostMapping({"/login", "/login/"})
    public String login(@Valid @ModelAttribute("tv") LoginRequest tv, BindingResult bindingResult, Model model,
                        HttpSession session) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("tv", tv);
            return "login/index";
        }

        ThanhVienResponse thanhVienResponse = authService.login(tv.getEmail(), tv.getPassword());

        if (thanhVienResponse == null) {
            bindingResult.rejectValue("credentials", "invalid.credentials", "Email hoặc mật khẩu không đúng.");
            return "login/index";
        } else {
            session.setAttribute("user", thanhVienResponse);
            return "redirect:/user";
        }
    }

    @GetMapping({"/register", "/register/"})
    public String registerForm(Model model) {
        model.addAttribute("tv", new RegisterRequest());
        return "register/index";
    }

    @PostMapping({"/register", "/register/"})
    public String register(@Valid @ModelAttribute("tv") RegisterRequest tv, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            if (!tv.isXacNhanMatKhauValid()) {
                bindingResult.rejectValue("xacNhanMatKhau", "password.mismatch", "Trường này không khớp với trường mật khẩu");
                bindingResult.rejectValue("matKhau", "password.mismatch", "Trường này không khớp với trường xác nhận mật khẩu");
            }
            model.addAttribute("tv", tv);
            return "register/index";
        }

        if (thanhVienService.getThanhVienById(Long.valueOf(tv.getMaTV())) != null) {
            bindingResult.rejectValue("credentials", "invalid.credentials", "Email đã tồn tại trong hệ thống");

            model.addAttribute("tv", tv);
            return "register/index";
        }

        if (thanhVienService.getThanhVienBySdt(tv.getSdt()) != null) {
            bindingResult.rejectValue("credentials", "invalid.credentials", "Số điện thoại đã tồn tại trong hệ thống");

            model.addAttribute("tv", tv);
            return "register/index";
        }

        if (!tv.isXacNhanMatKhauValid()) {
            bindingResult.rejectValue("xacNhanMatKhau", "password.mismatch", "Trường này không khớp với trường mật khẩu");
            bindingResult.rejectValue("matKhau", "password.mismatch", "Trường này không khớp với trường xác nhận mật khẩu");
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

        authService.register(thanhVien);

        // Logic to save the user to the database
        return "redirect:/login/";
    }

    @GetMapping({"/", ""})
    public String index(Model model, HttpSession session) {
        if (session.getAttribute("user") != null) {
            // Nếu có session với attribute là "user", chuyển hướng người dùng đến trang index
            // TODO SOMETHING ELSE
            return "/user/index";
        } else {
            // Nếu không có session "user", chuyển hướng người dùng đến trang đăng nhập
            // TODO SOMETHING ELSE
            return "redirect:/login";
        }
    }

    @GetMapping({"/admin/login", "/admin/login/"})
    public String adminLoginForm(Model model) {

        model.addAttribute("tv", new LoginRequest());

        return "admin/login/index";
    }

    @PostMapping({"/admin/login", "/admin/login/"})
    public String adminLogin(@Valid @ModelAttribute("tv") LoginRequest tv, BindingResult bindingResult, Model model,
                        HttpSession session) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("tv", tv);
            return "admin/login/index";
        }

        ThanhVienResponse thanhVienResponse = authService.adminLogin(tv.getEmail(), tv.getPassword());

        if (thanhVienResponse == null) {
            bindingResult.rejectValue("credentials", "invalid.credentials", "Email hoặc mật khẩu không đúng.");
            return "admin/login/index";
        } else {
            session.setAttribute("admin", thanhVienResponse);
            return "redirect:/admin";
        }
    }

    @GetMapping({"/quen-mat-khau", "/quen-mat-khau/"})
    public String forgotPassword(Model model, HttpSession session) {
        return "/quenmatkhau/index";
    }

    @PostMapping({"/quen-mat-khau", "/quen-mat-khau/"})
    public String forgotPasswordPost(Model model, HttpSession session) {
        if (session.getAttribute("user") != null) {
            // Nếu có session với attribute là "user", chuyển hướng người dùng đến trang index
            // TODO SOMETHING ELSE
            return "quenmatkhau/index";
        } else {
            // Nếu không có session "user", chuyển hướng người dùng đến trang đăng nhập
            // TODO SOMETHING ELSE
            return "redirect:/quen-mat-khau/index";
        }
    }

    @GetMapping({"/logout", "/logout/"})
    public String logout(Model model, HttpSession session) {
        if (session.getAttribute("user") != null) {
            session.removeAttribute("user");
        }
        return "redirect:/user";
    }

    @GetMapping({"/admin/logout", "/admin/logout/"})
    public String adminLogout(Model model, HttpSession session) {
        if (session.getAttribute("admin") != null) {
            session.removeAttribute("admin");
        }
        return "redirect:/admin";
    }
}
