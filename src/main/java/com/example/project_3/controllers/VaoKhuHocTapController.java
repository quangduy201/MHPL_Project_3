package com.example.project_3.controllers;

import com.example.project_3.models.ThanhVien;
import com.example.project_3.models.ThongTinSD;
import com.example.project_3.models.XuLy;
import com.example.project_3.payloads.requests.LoginRequest;
import com.example.project_3.payloads.requests.VaoKhuHocTapRequest;
import com.example.project_3.services.ThanhVienService;
import com.example.project_3.services.ThongTinSDService;
import com.example.project_3.services.XuLyService;
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

import java.time.Instant;
import java.util.List;

@Controller
@RequestMapping("/admin/vao-khu-hoc-tap")
public class VaoKhuHocTapController {
    @Autowired
    private XuLyService xuLyService;
    @Autowired
    private ThanhVienService thanhVienService;

    @Autowired
    private ThongTinSDService thongTinSDService;

    @GetMapping({"", "/"})
    public String index(Model model, HttpSession session) {
        if (session.getAttribute("admin") != null) {
            model.addAttribute("vkht", new VaoKhuHocTapRequest());
            return "admin/vaokhuhoctap/index";
        }

        return "redirect:/admin/login";
    }

    @PostMapping({"", "/"})
    public String vaoKhuHocTap(@Valid @ModelAttribute("vkht") VaoKhuHocTapRequest vkht, BindingResult bindingResult, Model model, HttpSession session) {
        if (session.getAttribute("admin") != null) {
            if (bindingResult.hasErrors()) {
                model.addAttribute("vkht", vkht);
                return "admin/vaokhuhoctap/index";
            }

            List<XuLy> xuLyList = xuLyService.getViPhamKhoaTaiKhoanByMaTVAnd(Long.valueOf(vkht.getMaTV()));
            if(!xuLyList.isEmpty()) {
                XuLy xuLy = xuLyList.get(0);
                bindingResult.rejectValue("credentials", "invalid.credentials", "Thành viên này đang bị vi phạm: " + xuLy.getHinhThucXL());
                model.addAttribute("vkht", vkht);
                return "admin/vaokhuhoctap/index";
            }

            ThanhVien tv = thanhVienService.getThanhVienById(Long.valueOf(vkht.getMaTV()));

            VaoKhuHocTapRequest vkhtRequest = new VaoKhuHocTapRequest(
                    "",
                    tv.getHoTen(),
                    tv.getKhoa(),
                    tv.getNganh(),
                    tv.getSdt(),
                    tv.getEmail(),
                    ""
            );

            ThongTinSD ttsd = new ThongTinSD(0, tv, null, Instant.now(), null, null, null, null);

            thongTinSDService.saveThongTinSD(ttsd);

            model.addAttribute("vkht", vkhtRequest);
            return "admin/vaokhuhoctap/index";
        }

        return "redirect:/admin/login";
    }
}
