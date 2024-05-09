package com.example.project_3.controllers;


import com.example.project_3.models.ThanhVien;
import com.example.project_3.models.ThietBi;
import com.example.project_3.models.ThongTinSD;
import com.example.project_3.payloads.requests.QuenMatKhauRequest;
import com.example.project_3.payloads.requests.ThanhVienRequest;
import com.example.project_3.payloads.responses.ThanhVienResponse;
import com.example.project_3.services.ThanhVienService;
import com.example.project_3.services.ThietBiService;
import com.example.project_3.services.ThongTinSDService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    private final ThietBiService thietBiService;
    private final ThanhVienService thanhvienService;
    private final ThongTinSDService thongTinSDService;

    @Autowired
    public UserController(ThietBiService thietBiService, ThanhVienService thanhvienService, ThongTinSDService thongTinSDService) {
        this.thietBiService = thietBiService;
        this.thanhvienService = thanhvienService;
        this.thongTinSDService = thongTinSDService;
    }

    @GetMapping({"/", ""})
    public String index(Model model, HttpSession session) {
        if (session.getAttribute("user") != null) {
            // Nếu có session với attribute là "user", chuyển hướng người dùng đến trang index
            // TODO SOMETHING ELSE
            ThanhVienResponse tvResponse = (ThanhVienResponse) session.getAttribute("user");
            model.addAttribute("tv",thanhvienService.getThanhVienById(tvResponse.getMaTV()));
            model.addAttribute("tvChangePassword", new QuenMatKhauRequest());

            return "user/index";
        } else {
            // Nếu không có session "user", chuyển hướng người dùng đến trang đăng nhập
            // TODO SOMETHING ELSE
            return "redirect:/login";
        }
    }

    @GetMapping({"/muon-thiet-bi", "/muon-thiet-bi/"})
    public String muonThietBi(Model model, @RequestParam Map<String, String> requestParam) {
        Page<ThietBi> thietBiList = thongTinSDService.showAllMuonThietBi(requestParam);
        requestParam.remove("page");
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry: requestParam.entrySet())
            builder.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue())
                    .append("&");
        if(!builder.isEmpty())
            builder.setLength(builder.length() -1);
        model.addAttribute("mtbList", thietBiList);
        model.addAttribute("params", builder.toString());
        return "/user/muonthietbi/index";
    }

    @GetMapping({"/thiet-bi", "/thiet-bi/"})
    public String showThietBi(Model model, @RequestParam Map<String, String> requestParam) {
        Page<ThietBi> thietBiList = thietBiService.getAllThietBi(requestParam);
        requestParam.remove("page");
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry: requestParam.entrySet())
            builder.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue())
                    .append("&");
        if(!builder.isEmpty())
            builder.setLength(builder.length() -1);
        model.addAttribute("tbList", thietBiList);
        model.addAttribute("params", builder.toString());
        return "/user/thietbi/index";
    }

    @GetMapping({"/dat-cho-thiet-bi", "/dat-cho-thiet-bi/"})
    public String datChoThietBi(Model model, @RequestParam Map<String, String> requestParam) {
        Page<ThietBi> thietBiList = thongTinSDService.showAllDatThietBi(requestParam);
        requestParam.remove("page");
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry: requestParam.entrySet())
            builder.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue())
                    .append("&");
        if(!builder.isEmpty())
            builder.setLength(builder.length() -1);
        model.addAttribute("dtbList", thietBiList);
        model.addAttribute("params", builder.toString());
        return "/user/datchothietbi/index";
    }

    @GetMapping({"/trang-thai-vi-pham", "/trang-thai-vi-pham/"})
    public String trangThaiViPham(Model model) {
        // TODO SOMETHING ELSE
        return "/user/trangthaivipham/index";
    }

    @PostMapping({"/", ""})
    public String editThanhVien(Model model,HttpSession httpSession,
                                @RequestParam Long maTV,
                                @Valid @ModelAttribute("tv") ThanhVienRequest tvRequest,
                                BindingResult result) {
        try {
            ThanhVien thanhVien = thanhvienService.getThanhVienById(maTV);

            if (result.hasErrors()) {
                model.addAttribute("tv",tvRequest);
                model.addAttribute("tvChangePassword", new QuenMatKhauRequest());

                return "user/index";
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

            model.addAttribute("tvChangePassword", new QuenMatKhauRequest());

            return "redirect:/user";
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        model.addAttribute("tvChangePassword", new QuenMatKhauRequest());

        return "user/index";
    }
    @PostMapping("/thaydoimatkhau")
    public String editMatKhau(@Valid @ModelAttribute("tvChangePassword") QuenMatKhauRequest tvChangePassword,
                              BindingResult result,
                              Model model,
                              HttpSession session) {
        ThanhVienResponse tvResponse = (ThanhVienResponse) session.getAttribute("user");
        ThanhVien thanhVien = thanhvienService.getThanhVienById(tvResponse.getMaTV());
        model.addAttribute("tv", thanhVien);

        boolean isError = result.hasErrors();

        if (!thanhVien.getPassword().equals(tvChangePassword.getMatKhauCu())) {
            result.rejectValue("matKhauCu", "password.mismatch", "Mật khẩu cũ không chính xác");
            isError = true;
        }

        if(!tvChangePassword.isXacNhanMatKhauMoiValid()){
            result.rejectValue("matKhauMoi", "password.mismatch", "Trường này không khớp với trường xác nhận mật khẩu mới");
            result.rejectValue("xacNhanMatKhauMoi", "password.mismatch", "Trường này không khớp với trường xác nhận mật khẩu mới");
            isError = true;
        }

        if (isError) {
            model.addAttribute("tvChangePassword", tvChangePassword);
            model.addAttribute("showFormChangePassword", true);

            return "user/index";
        }

        // Cập nhật mật khẩu mới cho thành viên và lưu vào cơ sở dữ liệu
        thanhVien.setPassword(tvChangePassword.getMatKhauMoi());

        thanhvienService.saveThanhVien(thanhVien);

        return "redirect:/user";
    }

}
