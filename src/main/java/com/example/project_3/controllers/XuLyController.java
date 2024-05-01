package com.example.project_3.controllers;

import com.example.project_3.models.ThanhVien;
import com.example.project_3.models.XuLy;
import com.example.project_3.payloads.requests.XuLyRequest;
import com.example.project_3.services.ThanhVienService;
import com.example.project_3.services.XuLyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin/xu-ly")
public class XuLyController {
    @Autowired
    private XuLyService xuLyService;
    @Autowired
    private ThanhVienService thanhVienService;

    @GetMapping({"", "/"})
    public String showAllXuLy(Model model) {
        addXuLyListToModel(model);
        model.addAttribute("xly", new XuLyRequest());
        model.addAttribute("showForm", false);
        return "admin/xuly/index";
    }

    @GetMapping("/edit")
    public ResponseEntity<?> showEdit(@RequestParam String maXL) {
        try {
            int id = Integer.parseInt(maXL);
            XuLy xl = xuLyService.getXuLyById(id);
            XuLyRequest xuLyRequest = XuLyRequest.builder()
                    .trangThaiXL(xl.getTrangThaiXL() == 1)
                    .hinhThucXL(xl.getHinhThucXL())
                    .ngayXL(LocalDateTime.ofInstant(xl.getNgayXL(), ZoneOffset.UTC))
                    .soTien(String.valueOf(xl.getSoTien()))
                    .maTV(String.valueOf(xl.getMaTV().getMaTV()))
                    .maXL(Integer.parseInt(maXL))
                    .build();
            return ResponseEntity.ok(xuLyRequest);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/edit")
    public String editXuLy(Model model,
                                @RequestParam String maXL,
                                @Valid @ModelAttribute("xly") XuLyRequest xly,
                                BindingResult result) {
        try {
            if (result.hasErrors()) {
                addXuLyListToModel(model);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                String formattedDate = xly.getNgayXL().format(formatter);
                model.addAttribute("ngayXL", formattedDate);

                model.addAttribute("showFormEdit", true);
                return "admin/xuly/index";
            }
            int id = Integer.parseInt(maXL);
            XuLy xl = xuLyService.getXuLyById(id);
            ThanhVien thanhVien = thanhVienService.getThanhVienById(Long.valueOf(xly.getMaTV()));
            xl.setMaTV(thanhVien);
            xl.setHinhThucXL(xly.getHinhThucXL());
            xl.setSoTien(Integer.valueOf(xly.getSoTien()));
            xl.setNgayXL(xly.getNgayXL().atZone(ZoneId.systemDefault()).toInstant());
            xl.setTrangThaiXL(xly.getTrangThaiXL() != null ? (xly.getTrangThaiXL() ? 1 : 0) : 0);
            xuLyService.saveXuLy(xl);
            model.addAttribute("showFormEdit", false);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/admin/xu-ly";
    }

    @PostMapping("/excel")
    public String excel(@RequestParam Object[][] rows) {
        try {
            for (Object[] row : rows) {
                ThanhVien tv = thanhVienService.getThanhVienById(Long.parseLong(row[1].toString()));
                String ngayXLString = row[4].toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date ngayXLDate = sdf.parse(ngayXLString);
                XuLy tb = new XuLy(Integer.parseInt(row[0].toString()), tv, row[2].toString(), Integer.parseInt(row[3].toString()), ngayXLDate.toInstant(), Integer.parseInt(row[5].toString()));
                xuLyService.saveXuLy(tb);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/admin/thiet-bi";
    }

    @GetMapping("/delete")
    public String deleteXuLy(@RequestParam String maXL) {
        try {
            int id = Integer.parseInt(maXL);
            xuLyService.deleteXuLyById(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/admin/xu-ly";
    }

    @PostMapping("/add")
    public String addXuLy(Model model, @Valid @ModelAttribute("xly") XuLyRequest xly, BindingResult result) {
        try {
            if (result.hasErrors()) {
                addXuLyListToModel(model);
                model.addAttribute("showForm", true);
                return "admin/xuly/index";
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
        } catch (Exception e) {
            System.out.println("Lỗi khi thêm mới xu ly: " + e.getMessage());
        }
        return "redirect:/admin/xu-ly";
    }

    private void addXuLyListToModel(Model model) {
        List<XuLy> xuLyList = xuLyService.getAllXuLy();
        Page<ThanhVien> thanhVienPage = thanhVienService.getThanhVien(Collections.singletonMap("all", ""));
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
    }
}
