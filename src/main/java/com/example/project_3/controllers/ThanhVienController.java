package com.example.project_3.controllers;

import com.example.project_3.models.ThanhVien;
import com.example.project_3.payloads.requests.ThanhVienRequest;
import com.example.project_3.services.ThanhVienService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/thanhvien")
public class ThanhVienController {
    private final ThanhVienService thanhvienService;

    @Autowired
    public ThanhVienController(ThanhVienService thanhvienService) {
        this.thanhvienService = thanhvienService;
    }

    @GetMapping({"", "/"})
    public String showAllThanhVien(Model model) {
        List<ThanhVien> thanhVienList = thanhvienService.getAllThanhVien();
        model.addAttribute("thanhVienList", thanhVienList);
        return "/admin/thanhvien/index";
    }

    @GetMapping("/edit")
    public String showEdit(Model model, @RequestParam String maTV) {
        try {
            Long id = Long.parseLong(maTV);
            ThanhVien thanhVien = thanhvienService.getThanhVienById(id);
            model.addAttribute("thanhVien", thanhVien);

            ThanhVienRequest thanhVienRequest = ThanhVienRequest.builder()
                    .hoTen(thanhVien.getHoTen())
                    .khoa(thanhVien.getKhoa())
                    .nganh(thanhVien.getNganh())
                    .sdt(thanhVien.getSdt())
                    .email(thanhVien.getEmail())
                    .build();
            model.addAttribute("tvRequest", thanhVienRequest);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "redirect:/admin/thanhvien";
        }
        return "/admin/thanhvien/edit";
    }

    @PostMapping("/edit")
    public String editThanhVien(Model model,
                                @RequestParam String maTV,
                                @Valid @ModelAttribute("tvRequest") ThanhVienRequest tvRequest,
                                BindingResult result) {
        try {
            Long id = Long.parseLong(maTV);
            ThanhVien thanhVien = thanhvienService.getThanhVienById(id);
            model.addAttribute("thanhVien", thanhVien);

            if (result.hasErrors()) {
                return "/admin/thanhvien/edit";
            }

            thanhVien.setHoTen(tvRequest.getHoTen());
            thanhVien.setKhoa(tvRequest.getKhoa());
            thanhVien.setNganh(tvRequest.getNganh());
            thanhVien.setSdt(tvRequest.getSdt());
            thanhVien.setEmail(tvRequest.getEmail());

            thanhvienService.saveThanhVien(thanhVien);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/admin/thanhvien";
    }

    @GetMapping("/delete")
    public String deleteThanhVien(@RequestParam String maTV) {
        try {
            Long id = Long.parseLong(maTV);
            thanhvienService.deleteThanhVienById(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/admin/thanhvien";
    }
}
