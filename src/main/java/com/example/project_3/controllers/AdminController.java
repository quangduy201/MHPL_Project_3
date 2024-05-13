package com.example.project_3.controllers;

import com.example.project_3.models.ThanhVien;
import com.example.project_3.models.ThietBi;
import com.example.project_3.models.ThongTinSD;
import com.example.project_3.models.XuLy;
import com.example.project_3.payloads.requests.XuLyRequest;
import com.example.project_3.services.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private ThongKeService thongKeService;

    @Autowired
    private ThanhVienService thanhVienService;
    @Autowired
    private ThongTinSDService thongTinSDService;

    @Autowired
    private ThietBiService thietBiService;

    @Autowired
    private XuLyService xuLyService;

    @GetMapping({"", "/"})
    public String index(Model model, HttpSession session) {
        if (session.getAttribute("admin") != null) {
            // Nếu có session với attribute là "admin", chuyển hướng người dùng đến trang index
            List<String> khoaList = thanhVienService.getKhoaList();
            List<String> nganhList = thanhVienService.getNganhList();
            List<ThietBi> thietBis = thietBiService.getAllThietBi();

            model.addAttribute("khoa", khoaList);
            model.addAttribute("nganh", nganhList);
            model.addAttribute("thietbi", thietBis);

            return "/admin/index";
        } else {
            // Nếu không có session "user", chuyển hướng người dùng đến trang đăng nhập
            // TODO SOMETHING ELSE
            return "redirect:/admin/login";
        }
    }

    @GetMapping({"/thong-tin-su-dung", "/thong-tin-su-dung/"})
    public String ttsd(Model model, HttpSession session) {
        if (session.getAttribute("admin") != null) {
            // Nếu có session với attribute là "admin", chuyển hướng người dùng đến trang index
            List<ThongTinSD> thongTinSD = thongTinSDService.getAllThongTinSD();
            List<XuLy> xuLyList = xuLyService.getAllXuLy();
            Page<ThanhVien> thanhVienPage = thanhVienService.getThanhVien(Collections.singletonMap("all", ""));
            List<String> htList = List.of(
                    "Khóa thẻ 1 tháng",
                    "Khóa thẻ 2 tháng",
                    "Khóa thẻ 3 tháng",
                    "Bồi thường mất tài sản",
                    "Khóa thẻ 1 tháng và bồi thường"
            );
            model.addAttribute("thanhVienList", thanhVienPage);
            model.addAttribute("htList", htList);
            model.addAttribute("xly", new XuLyRequest());
            model.addAttribute("showForm", false);
            model.addAttribute("ttsd", thongTinSD);

            return "admin/datcho/index";
        } else {
            // Nếu không có session "user", chuyển hướng người dùng đến trang đăng nhập
            // TODO SOMETHING ELSE
            return "redirect:/admin/login";
        }
    }

    @PostMapping({"/thong-tin-su-dung", "/thong-tin-su-dung/"})
    public String addXuLy(Model model, @Valid @ModelAttribute("xly") XuLyRequest xly, BindingResult result, HttpSession session) {
        if (session.getAttribute("admin") != null) {
            try {
                if (result.hasErrors()) {
                    addXuLyListToModel(model);
                    model.addAttribute("showForm", true);
                    return "admin/datcho/index";
                }
                XuLy xuLy = new XuLy();
                ThanhVien thanhVien = thanhVienService.getThanhVienById(Long.valueOf(xly.getMaTV()));
                xuLy.setMaTV(thanhVien);
                xuLy.setHinhThucXL(xly.getHinhThucXL());
                xuLy.setSoTien(Integer.valueOf(xly.getSoTien()));
                xuLy.setNgayXL(xly.getNgayXL().atZone(ZoneId.systemDefault()).toInstant());
                xuLy.setTrangThaiXL(xly.getTrangThaiXL() != null ? (xly.getTrangThaiXL() ? 1 : 0) : 0);
                xuLyService.saveXuLy(xuLy);
                model.addAttribute("showForm", false);
                return "admin/datcho/index";
            } catch (Exception e) {
                System.out.println("Lỗi khi thêm mới xu ly: " + e.getMessage());
            }
        }
        return "redirect:/admin/login";
    }

    private void addXuLyListToModel(Model model) {
        List<XuLy> xuLyList = xuLyService.getAllXuLy();
        Page<ThanhVien> thanhVienPage = thanhVienService.getThanhVien(Collections.singletonMap("all", ""));
        List<ThongTinSD> thongTinSD = thongTinSDService.getAllThongTinSD();
        List<String> htList = List.of(
                "Khóa thẻ 1 tháng",
                "Khóa thẻ 2 tháng",
                "Khóa thẻ 3 tháng",
                "Bồi thường mất tài sản",
                "Khóa thẻ 1 tháng và bồi thường"
        );
        model.addAttribute("xuLyList", xuLyList);
        model.addAttribute("thanhVienList", thanhVienPage);
        model.addAttribute("htList", htList);
        model.addAttribute("ttsd", thongTinSD);
    }

    @PostMapping("/dashboard/member")
    @ResponseBody
    public List<List<Object[]>> getMemberChart(@RequestParam(required = false) String dateRange,
                                          @RequestParam(required = false) String khoa,
                                          @RequestParam(required = false) String nganh) {
        return List.of(getMemberData(dateRange, khoa, nganh, false), getMemberData(dateRange, khoa, nganh, true));
    }

    private List<Object[]> getMemberData(String dateRange, String khoa, String nganh, boolean isTable) {
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;

        if (dateRange != null && !dateRange.isEmpty()) {
            String[] dates = dateRange.split(" - ");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            startTime = LocalDate.parse(dates[0], formatter).atStartOfDay();
            endTime = LocalDate.parse(dates[1], formatter).atStartOfDay();
        } else {
            startTime = LocalDateTime.now().minusDays(30);
            endTime = LocalDateTime.now();
        }

        return thongKeService.thongKeThanhVienVaoKhuHocTap(startTime, endTime, khoa, nganh, isTable);
    }

    @PostMapping("/dashboard/equipment-1")
    @ResponseBody
    public List<List<Object[]>> getEquipmentChartV1(@RequestParam(required = false) String dateRange,
                                               @RequestParam(required = false) String maTB) {
        return List.of(getEquipmentData(dateRange, maTB, false, true), getEquipmentData(dateRange, maTB, true, true));
    }

    @PostMapping("/dashboard/equipment-2")
    @ResponseBody
    public List<List<Object[]>> getEquipmentChartV2(@RequestParam(required = false) String dateRange,
                                                    @RequestParam(required = false) String maTB) {
        return List.of(getEquipmentData(dateRange, maTB, false, false), getEquipmentData(dateRange, maTB, true, false));
    }

    private List<Object[]> getEquipmentData(String dateRange, String maTB, boolean isTable, boolean isHired) {
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;

        if (dateRange != null && !dateRange.isEmpty()) {
            String[] dates = dateRange.split(" - ");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            startTime = LocalDate.parse(dates[0], formatter).atStartOfDay();
            endTime = LocalDate.parse(dates[1], formatter).atStartOfDay();
        } else {
            startTime = LocalDateTime.now().minusDays(30);
            endTime = LocalDateTime.now();
        }

        if (isHired) {
            return thongKeService.thongKeThietBiDaDuocMuon(startTime, endTime, maTB, isTable);
        }

        return thongKeService.thongKeThietBiDangDuocMuon(startTime, endTime, maTB, isTable);
    }

    @PostMapping("/dashboard/breach-1")
    @ResponseBody
    public List<List<Object[]>> getBreachChartV1(@RequestParam(required = false) String dateRange,
                                                    @RequestParam(required = false) String maTB) {
        return List.of(getBreachData(dateRange, maTB, false, true), getBreachData(dateRange, maTB, true, true));
    }

    @PostMapping("/dashboard/breach-2")
    @ResponseBody
    public List<List<Object[]>> getBreachChartV2(@RequestParam(required = false) String dateRange,
                                                    @RequestParam(required = false) String maTB) {
        return List.of(getBreachData(dateRange, maTB, false, false), getBreachData(dateRange, maTB, true, false));
    }

    private List<Object[]> getBreachData(String dateRange, String maTB, boolean isTable, boolean trangThai) {
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;

        if (dateRange != null && !dateRange.isEmpty()) {
            String[] dates = dateRange.split(" - ");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            startTime = LocalDate.parse(dates[0], formatter).atStartOfDay();
            endTime = LocalDate.parse(dates[1], formatter).atStartOfDay();
        } else {
            startTime = LocalDateTime.now().minusDays(30);
            endTime = LocalDateTime.now();
        }

        return thongKeService.thongKeXuLy(startTime, endTime, trangThai, isTable);
    }
}
