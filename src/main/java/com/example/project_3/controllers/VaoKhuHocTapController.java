package com.example.project_3.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/vao-khu-hoc-tap")
public class VaoKhuHocTapController {
    @GetMapping({"", "/"})
    public String index(Model model, HttpSession session) {
        if (session.getAttribute("admin") != null) {
            return "/admin/vaokhuhoctap/index";
        }

        return "redirect:/admin/login";
    }

}
