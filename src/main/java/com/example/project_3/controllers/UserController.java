package com.example.project_3.controllers;


import com.example.project_3.models.ThanhVien;
import com.example.project_3.models.ThietBi;
import com.example.project_3.payloads.requests.RegisterRequest;
import com.example.project_3.payloads.requests.ThanhVienRequest;
import com.example.project_3.payloads.requests.ThietBiRequest;
import com.example.project_3.payloads.responses.ThanhVienResponse;
import com.example.project_3.services.ThanhVienService;
import com.example.project_3.services.ThietBiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private ThanhVienService thanhvienService;
    @GetMapping({"/", ""})
    public String index(Model model, HttpSession session) {
        if (session.getAttribute("user") != null) {
            // Nếu có session với attribute là "user", chuyển hướng người dùng đến trang index
            // TODO SOMETHING ELSE
            ThanhVienResponse tvResponse = (ThanhVienResponse) session.getAttribute("user");
            model.addAttribute("tv",thanhvienService.getThanhVienById(tvResponse.getMaTV()));
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
        return "/user/thaydoimatkhau/index.html";
    }

    @GetMapping({"/trang-thai-vi-pham", "/trang-thai-vi-pham/"})
    public String trangThaiViPham(Model model) {
        // TODO SOMETHING ELSE
        return "/user/trangthaivipham/index";
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
            return "redirect:/user";
        }
        return "/user";
    }

    @PostMapping("/edit")
    public String editThanhVien(Model model,HttpSession httpSession,
                                @RequestParam Long maTV,
                                @Valid @ModelAttribute("tv") ThanhVienRequest tvRequest,
                                BindingResult result) {
        try {
            ThanhVien thanhVien = thanhvienService.getThanhVienById(maTV);
            model.addAttribute("thanhVien", thanhVien);

            if (result.hasErrors()) {
                model.addAttribute("tv",tvRequest);
                return "/user/index";
            }

            // Cập nhật thông tin của thành viên
            thanhVien.setHoTen(tvRequest.getHoTen());
            thanhVien.setKhoa(tvRequest.getKhoa());
            thanhVien.setNganh(tvRequest.getNganh());
            thanhVien.setSdt(tvRequest.getSdt());
            thanhVien.setEmail(tvRequest.getEmail());

            // Lưu thông tin đã cập nhật vào cơ sở dữ liệu
            thanhvienService.saveThanhVien(thanhVien);

            // Cập nhật session.user với thông tin mới
            ThanhVienResponse thanhVienResponse = new ThanhVienResponse();
            thanhVienResponse.setMaTV(maTV);
            thanhVienResponse.setHoTen(tvRequest.getHoTen());
            thanhVienResponse.setKhoa(tvRequest.getKhoa());
            thanhVienResponse.setNganh(tvRequest.getNganh());
            thanhVienResponse.setSdt(tvRequest.getSdt());
            thanhVienResponse.setEmail(tvRequest.getEmail());
            httpSession.setAttribute("user", thanhVienResponse);

            ObjectMapper objectMapper = new ObjectMapper();
            System.out.println(objectMapper.writeValueAsString(thanhVien));
            System.out.println(objectMapper.writeValueAsString(tvRequest));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/user";
    }
    @PostMapping("/thaydoimatkhau")
    public String editMatKhau(Model model,
                              @Valid @ModelAttribute("tv") ThanhVienRequest tv,
                              @RequestParam Long maTV,
                              @RequestParam String currentPassword,
                              BindingResult bindingResult,
                              @RequestParam String newPassword,
                              @RequestParam String confirmPassword) {
        try {
            ThanhVien thanhVien = thanhvienService.getThanhVienById(maTV);
            model.addAttribute("thanhVien", thanhVien);

            // Kiểm tra tính hợp lệ của mật khẩu hiện tại
            if (!currentPassword.equals(thanhVien.getPassword())) {
                if(!tv.isXacNhanMatKhauMoiValid()){
                    bindingResult.rejectValue("newPassword", "password.mismatch", "Trường này không khớp với trường xác nhận mật khẩu");
                    bindingResult.rejectValue("confirmPassword", "password.mismatch", "Trường này không khớp với trường mật khẩu mới");
                }
                return "/user/thaydoimatkhau";
            }

            if (!newPassword.equals(confirmPassword)) {
                if(!tv.isXacNhanMatKhauCuValid(currentPassword)){
                    bindingResult.rejectValue("currentPassword", "password.mismatch", "Mật khẩu cũ không chính xác");
                }
                return "/user/thaydoimatkhau";
            }

            // Cập nhật mật khẩu mới cho thành viên và lưu vào cơ sở dữ liệu
            thanhVien.setPassword(newPassword);
            thanhvienService.saveThanhVien(thanhVien);

            return "redirect:/user";
        } catch (Exception e) {
            // Xử lý các ngoại lệ nếu có
            System.out.println(e.getMessage());
            return "redirect:/user/thay-doi-mat-khau";
        }
    }


}
