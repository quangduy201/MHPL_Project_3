package com.example.project_3.controllers;


import jakarta.servlet.http.HttpSession;
<<<<<<< Updated upstream
=======
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
>>>>>>> Stashed changes
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {
<<<<<<< Updated upstream

=======
    @Autowired
    private ThanhVienService thanhvienService;
    @Autowired
private ThietBiService thietbiService;
>>>>>>> Stashed changes
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

//    @GetMapping({"/muon-thiet-bi", "/muon-thiet-bi/"})
//    public String muonThietBi(Model model, HttpSession session, @RequestParam("maTV") Long maTV) {
//        try {
//            List<ThietBi> borrowedDevices = thietbiService.getAllThietBiDangMuonById(maTV);
//
//            if (!borrowedDevices.isEmpty()) {
//                model.addAttribute("borrowedDevices", borrowedDevices);
//            } else {
//                model.addAttribute("borrowedDevices", Collections.emptyList());
//            }
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            return "redirect:/user";
//        }
//
//        return "/user/muonthietbi/index";
//    }
    @GetMapping({"/muon-thiet-bi", "/muon-thiet-bi/"})
    public String muonThietBi(Model model, HttpSession session, @RequestParam("maTV") Long maTV) {
        Page<ThietBi> thietBiDangMuon = thietbiService.getThietBiDangMuonByMaTV(maTV);
        model.addAttribute("thietBiDangMuon", thietBiDangMuon);
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
