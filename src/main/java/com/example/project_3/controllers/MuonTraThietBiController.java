package com.example.project_3.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/muon-tra-thiet-bi")
public class MuonTraThietBiController {
    @GetMapping({"", "/"})
    public String index(Model model, HttpSession session) {
        if (session.getAttribute("admin") != null) {
            return "/admin/muontrathietbi/index";
        }

        return "redirect:/admin/login";
    }

}
