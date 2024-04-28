package com.example.project_3.controllers;

import com.example.project_3.models.ThanhVien;
import com.example.project_3.models.XuLy;
import com.example.project_3.payloads.requests.XuLyRequest;
import com.example.project_3.services.ThanhVienService;
import com.example.project_3.services.XuLyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
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
        List<XuLy> xuLyList = xuLyService.getAllXuLy();
        model.addAttribute("xuLyList", xuLyList);
        return "admin/xuly/index";
    }

    @GetMapping("/edit")
    public String showEdit(Model model, @RequestParam String maXL) {
        try {
            int id = Integer.parseInt(maXL);
            XuLy xl = xuLyService.getXuLyById(id);
            model.addAttribute("xuLy", xl);

            XuLyRequest xuLyRequest = XuLyRequest.builder()
                    .hinhThucXL(xl.getHinhThucXL())
                    .ngayXL(xl.getNgayXL().toString())
                    .soTien(String.valueOf(xl.getSoTien()))
                    .maTV(String.valueOf(xl.getMaTV()))
                    .build();
            model.addAttribute("xlRequest", xuLyRequest);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "redirect:/admin/xu-ly";
        }
        return "admin/xuly/edit";
    }

    @PostMapping("/edit")
    public String editXuLy(Model model,
                                @RequestParam String maXL,
                                @Valid @ModelAttribute("xlRequest") XuLyRequest xlRequest,
                                BindingResult result) {
        try {
            int id = Integer.parseInt(maXL);
            XuLy xl = xuLyService.getXuLyById(id);
            model.addAttribute("xuLy", xl);

            if (result.hasErrors()) {
                return "admin/xuly/edit";
            }

            ThanhVien tv = thanhVienService.getThanhVienById(Long.parseLong(xlRequest.getMaTV()));

            String ngayXLString = xlRequest.getNgayXL();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Date ngayXLDate = sdf.parse(ngayXLString);

            xl.setMaTV(tv);
            xl.setNgayXL(ngayXLDate.toInstant());
            xl.setTrangThaiXL(xlRequest.getTrangThaiXL());
            xl.setHinhThucXL(xlRequest.getHinhThucXL());

            xuLyService.saveXuLy(xl);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/admin/xu-ly";
    }

    @GetMapping("/delete")
    public String deleteXuLy(@RequestParam String maTB) {
        try {
            int id = Integer.parseInt(maTB);
            xuLyService.deleteXuLyById(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/admin/xu-ly";
    }
}
