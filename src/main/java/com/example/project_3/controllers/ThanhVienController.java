package com.example.project_3.controllers;

import com.example.project_3.models.ThanhVien;
import com.example.project_3.payloads.requests.ThanhVienRequest;
import com.example.project_3.services.ThanhVienService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/thanhvien")
public class ThanhVienController {
    private final ThanhVienService thanhvienService;

    @Autowired
    public ThanhVienController(ThanhVienService thanhvienService) {
        this.thanhvienService = thanhvienService;
    }

    @GetMapping({"", "/"})
    public String showThanhVien(Model model, @RequestParam Map<String, String> requestParams) {

        Page<ThanhVien> thanhVienList = thanhvienService.getThanhVien(requestParams);
        requestParams.remove("page");
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : requestParams.entrySet())
            builder.append(entry.getKey())
                    .append('=')
                    .append(entry.getValue())
                    .append('&');
        if (!builder.isEmpty())
            builder.setLength(builder.length() - 1); // remove the last '&'
        model.addAttribute("tvList", thanhVienList);
        model.addAttribute("params", builder.toString());
        return "thanhvien/index";
    }

    @GetMapping("/edit")
    public String showEdit(Model model, @RequestParam Long maTV) {
        try {
            ThanhVien thanhVien = thanhvienService.getThanhVienById(maTV);
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
            return "redirect:/thanhvien";
        }
        return "thanhvien/edit";
    }

    @PostMapping("/edit")
    public String editThanhVien(Model model,
                                @RequestParam Long maTV,
                                @Valid @ModelAttribute("tvRequest") ThanhVienRequest tvRequest,
                                BindingResult result) {
        try {
            ThanhVien thanhVien = thanhvienService.getThanhVienById(maTV);
            model.addAttribute("thanhVien", thanhVien);

            if (result.hasErrors()) {
                return "thanhvien/edit";
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
        return "redirect:/thanhvien";
    }

    @GetMapping("/delete")
    public String deleteThanhVien(@RequestParam String maTV) {
        try {
            Long id = Long.parseLong(maTV);
            thanhvienService.deleteThanhVienById(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/thanhvien";
    }
}
