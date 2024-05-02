package com.example.project_3.controllers;


import com.example.project_3.models.ThanhVien;
import com.example.project_3.payloads.requests.LoginRequest;
import com.example.project_3.payloads.requests.RegisterRequest;
import com.example.project_3.payloads.responses.ThanhVienResponse;
import com.example.project_3.services.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping({  "/login/", "/login"})
    public String index(Model model) {

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
    public String getForm(Model model) {
        model.addAttribute("tv", new RegisterRequest());
        return "register/index";
    }

    @PostMapping({"/register", "/register/"})
    public String registerSubmit(@Valid @ModelAttribute("tv") RegisterRequest tv, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
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

        authService.register(thanhVien);

        // Logic to save the user to the database
        return "redirect:/user/" + thanhVien.getMaTV();
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
    public String loginAdmin(Model model, HttpSession session) {
        if (session.getAttribute("user") != null) {
            // Nếu có session với attribute là "user", chuyển hướng người dùng đến trang index
            // TODO SOMETHING ELSE
            return "/admin/index";
        } else {
            // Nếu không có session "user", chuyển hướng người dùng đến trang đăng nhập
            // TODO SOMETHING ELSE
            return "redirect:/admin/login";
        }
    }

    @PostMapping({"/admin/login", "/admin/login/"})
    public String loginAdminPost(Model model, HttpSession session) {
        if (session.getAttribute("user") != null) {
            // Nếu có session với attribute là "user", chuyển hướng người dùng đến trang index
            // TODO SOMETHING ELSE
            return "/admin/index";
        } else {
            // Nếu không có session "user", chuyển hướng người dùng đến trang đăng nhập
            // TODO SOMETHING ELSE
            return "redirect:/admin/login";
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
}
