package com.example.project_3.controllers;

import com.example.project_3.models.ThanhVien;
import com.example.project_3.models.ThietBi;
import com.example.project_3.payloads.requests.ThanhVienRequest;
import com.example.project_3.services.ThanhVienService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/thanh-vien")
public class ThanhVienController {
    private final ThanhVienService thanhvienService;

    @Autowired
    public ThanhVienController(ThanhVienService thanhvienService) {
        this.thanhvienService = thanhvienService;
    }

    @GetMapping({"", "/"})
    public String showAllThanhVien(Model model) {
        addThanhVienListToModel(model);
        model.addAttribute("tv", new ThanhVienRequest());
        model.addAttribute("showForm", false);
        return "/admin/thanhvien/index";
    }

    @GetMapping("/edit")
    public ResponseEntity<?> showEdit(@RequestParam String maTV) {
        try {
            Long id = Long.parseLong(maTV);
            ThanhVien tv = thanhvienService.getThanhVienById(id);
            ThanhVienRequest thanhVienRequest = ThanhVienRequest.builder()
                    .maTV(tv.getMaTV())
                    .hoTen(tv.getHoTen())
                    .khoa(tv.getKhoa())
                    .nganh(tv.getNganh())
                    .sdt(tv.getSdt())
                    .email(tv.getEmail())
                    .build();
            return ResponseEntity.ok(thanhVienRequest);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while fetching data.");
        }
    }

    @PostMapping("/edit")
    public String editThanhVien(Model model,
                                @RequestParam String maTV,
                                @Valid @ModelAttribute("tv") ThanhVienRequest tv,
                                BindingResult result, HttpSession session) {
        if (session.getAttribute("admin") != null) {
            try {
                if (result.hasErrors()) {
                    addThanhVienListToModel(model);
                    model.addAttribute("showFormEdit", true);
                    return "/admin/thanhvien/index";
                }

                Long id = Long.parseLong(maTV);
                ThanhVien thanhVien = thanhvienService.getThanhVienById(id);
                thanhVien.setHoTen(tv.getHoTen());
                thanhVien.setKhoa(tv.getKhoa());
                thanhVien.setNganh(tv.getNganh());
                thanhVien.setSdt(tv.getSdt());
                thanhVien.setEmail(tv.getEmail());
                thanhvienService.saveThanhVien(thanhVien);
                model.addAttribute("showFormEdit", false);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return "redirect:/admin/thanh-vien";
        } else {
            return "redirect:/admin/login";
        }
    }

    @PostMapping("/excel")
    public String excel(@RequestParam Object[][] rows) {
        try {
            for (Object[] row : rows) {
                ThanhVien tv = ThanhVien.builder()
                        .maTV(Long.parseLong(row[0].toString()))
                        .hoTen(row[1].toString())
                        .khoa(row[2].toString())
                        .nganh(row[3].toString())
                        .sdt(row[4].toString())
                        .email(row[5].toString())
                        .build();
                thanhvienService.saveThanhVien(tv);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/admin/thanh-vien";
    }

    @DeleteMapping("/delete")
    public String deleteThanhVien(@RequestParam String maTV) {
        try {
            Long id = Long.parseLong(maTV);
            thanhvienService.deleteThanhVienById(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/admin/thanh-vien";
    }

    @PostMapping({"/", ""})
    public String addThanhVien(@Valid @ModelAttribute("tv") ThanhVienRequest tv,
                               BindingResult result,
                               Model model) {
        try {
            if (result.hasErrors()) {
                addThanhVienListToModel(model);
                model.addAttribute("showForm", true);
                return "/admin/thanhvien/index";
            }

            if (existsByMaTV(tv.getMaTV().toString())) {
                result.rejectValue("maTV", "error.tv", "Mã thành viên đã tồn tại");
                addThanhVienListToModel(model);
                model.addAttribute("showForm", true);
                return "/admin/thanhvien/index";
            }

            ThanhVien thanhVien = ThanhVien.builder()
                    .maTV(tv.getMaTV())
                    .hoTen(tv.getHoTen())
                    .khoa(tv.getKhoa())
                    .nganh(tv.getNganh())
                    .sdt(tv.getSdt())
                    .email(tv.getEmail())
                    .build();
            thanhvienService.saveThanhVien(thanhVien);
            model.addAttribute("showForm", false);
        } catch (Exception e) {
            System.out.println("Lỗi khi thêm thành viên: " + e.getMessage());
        }
        return "redirect:/admin/thanh-vien";
    }

    private boolean existsByMaTV(String maTV) {
        ThanhVien thanhVien = thanhvienService.getThanhVienById(Long.parseLong(maTV));
        return thanhVien != null;
    }

    private void addThanhVienListToModel(Model model) {
        List<ThanhVien> thanhVienList = thanhvienService.getAllThanhVien();
        model.addAttribute("thanhVienList", thanhVienList);
    }
}
