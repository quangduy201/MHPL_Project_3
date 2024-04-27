package com.example.project_3.controllers;

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

    @GetMapping({"", "/"})
    public String index(Model model) {
        String dateRange = (String) model.getAttribute("dateRange");
        String khoa = (String) model.getAttribute("khoa");
        String nganh = (String) model.getAttribute("nganh");

        List<Object[]> data = getData(dateRange, khoa, nganh, false);

        model.addAttribute("data", data);
        return "admin/index";
    }

    @PostMapping({"/", ""})
    @ResponseBody
    public List<List<Object[]>> updateChartData(@RequestParam(required = false) String dateRange,
                                          @RequestParam(required = false) String khoa,
                                          @RequestParam(required = false) String nganh) {
        return List.of(getData(dateRange, khoa, nganh, false), getData(dateRange, khoa, nganh, true));
    }

    private List<Object[]> getData(String dateRange, String khoa, String nganh, boolean isTable) {
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
}
