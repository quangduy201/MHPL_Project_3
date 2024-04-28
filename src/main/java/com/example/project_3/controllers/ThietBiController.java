package com.example.project_3.controllers;

import com.example.project_3.models.ThietBi;
import com.example.project_3.payloads.requests.ThietBiRequest;
import com.example.project_3.services.ThietBiService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/thiet-bi")
public class ThietBiController {
    @Autowired
    private ThietBiService thietBiService;

    @GetMapping({"", "/"})
    public String showAllThietBi(Model model) {
        List<ThietBi> thietBiList = thietBiService.getAllThietBi();
        model.addAttribute("thietBiList", thietBiList);
        return "/admin/thietbi/index";
    }

    @GetMapping("/edit")
    public String showEdit(Model model, @RequestParam String maTB) {
        try {
            Long id = Long.parseLong(maTB);
            ThietBi tb = thietBiService.getThietBiById(id);
            model.addAttribute("thietBi", tb);

            ThietBiRequest thietBiRequest = ThietBiRequest.builder()
                    .moTaTB(tb.getMoTaTB())
                    .tenTB(tb.getTenTB())
                    .build();
            model.addAttribute("tbRequest", thietBiRequest);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "redirect:/admin/thiet-bi";
        }
        return "/admin/thanhvien/edit";
    }

    @PostMapping("/edit")
    public String editThietBi(Model model,
                                @RequestParam String maTB,
                                @Valid @ModelAttribute("tbRequest") ThietBiRequest tbRequest,
                                BindingResult result) {
        try {
            Long id = Long.parseLong(maTB);
            ThietBi tb = thietBiService.getThietBiById(id);
            model.addAttribute("thietBi", tb);

            if (result.hasErrors()) {
                return "/admin/thietbi/edit";
            }

            tb.setTenTB(tbRequest.getTenTB());
            tb.setMoTaTB(tbRequest.getMoTaTB());

            thietBiService.saveThietBi(tb);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/admin/thiet-bi";
    }

    @PostMapping("/excel")
    public String excel(@RequestParam Object[][] rows) {
        try {
            for (Object[] row : rows) {
                ThietBi tb = new ThietBi(Long.parseLong(row[0].toString()), row[1].toString(), row[2].toString());
                thietBiService.saveThietBi(tb);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/admin/thiet-bi";
    }

    @GetMapping("/delete")
    public String deleteThietBi(@RequestParam String maTB) {
        try {
            Long id = Long.parseLong(maTB);
            thietBiService.deleteThietBiById(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/admin/thiet-bi";
    }
}
