package com.example.project_3.controllers;

import com.example.project_3.models.ThietBi;
import com.example.project_3.services.ThanhVienService;
import com.example.project_3.services.ThietBiService;
import com.example.project_3.services.ThongKeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private ThongKeService thongKeService;

    @Autowired
    private ThanhVienService thanhVienService;

    @Autowired
    private ThietBiService thietBiService;

    @GetMapping({"", "/"})
    public String index(Model model) {
        List<String> khoaList = thanhVienService.getKhoaList();
        List<String> nganhList = thanhVienService.getNganhList();
        List<ThietBi> thietBis = thietBiService.getAllThietBi();

        model.addAttribute("khoa", khoaList);
        model.addAttribute("nganh", nganhList);
        model.addAttribute("thietbi", thietBis);

        return "admin/index";
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
