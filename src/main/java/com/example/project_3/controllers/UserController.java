package com.example.project_3.controllers;


import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

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

    @GetMapping({"/muon-thiet-bi", "/muon-thiet-bi/"})
    public String muonThietBi(Model model, HttpSession session) {
        // TODO SOMETHING ELSE
        return "/user/muonthietbi/index";
    }

    @GetMapping({"/dat-cho-thiet-bi", "/dat-cho-thiet-bi/"})
    public String datChoThietBi(Model model, HttpSession session) {
        // TODO SOMETHING ELSE
        return "/user/datchothietbi/index";
    }

    @GetMapping({"/thay-doi-mat-khau", "/thay-doi-mat-khau/"})
    public String changePassword(Model model, HttpSession session) {
        // TODO SOMETHING ELSE
        return "/user/thaydoimatkhay/index.html";
    }

    @GetMapping({"/trang-thai-vi-pham", "/trang-thai-vi-pham/"})
    public String trangThaiViPham(Model model) {
        // TODO SOMETHING ELSE
        return "/user/trangthaivipham/index";
    }
}
