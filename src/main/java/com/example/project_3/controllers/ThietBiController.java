package com.example.project_3.controllers;

import com.example.project_3.models.ThietBi;
import com.example.project_3.payloads.requests.ThietBiRequest;
import com.example.project_3.services.ThietBiService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
        model.addAttribute("showForm", false);
        model.addAttribute("tb", new ThietBiRequest());
        return "/admin/thietbi/index";
    }

    @GetMapping("/edit")
    public ResponseEntity<?> showEdit(@RequestParam String maTB) {
        try {
            Long id = Long.parseLong(maTB);
            ThietBi tb = thietBiService.getThietBiById(id);
            ThietBiRequest thietBiRequest = ThietBiRequest.builder()
                    .moTaTB(tb.getMoTaTB())
                    .tenTB(tb.getTenTB())
                    .maTB(tb.getMaTB())
                    .build();
            return ResponseEntity.ok(thietBiRequest);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while fetching data.");
        }
    }

    @PostMapping("/edit")
    public String editThietBi(Model model,
                                @RequestParam String maTB,
                                @Valid @ModelAttribute("tb") ThietBiRequest tb,
                                BindingResult result) {
        try {

            if (result.hasErrors()) {
                List<ThietBi> thietBiList = thietBiService.getAllThietBi();
                model.addAttribute("thietBiList", thietBiList);
                model.addAttribute("showFormEdit", true);
                return "/admin/thietbi/index";
            }
            Long id = Long.parseLong(maTB);
            ThietBi thietbi = thietBiService.getThietBiById(id);
            model.addAttribute("showFormEdit", false);
            thietbi.setTenTB(tb.getTenTB());
            thietbi.setMoTaTB(tb.getMoTaTB());

            thietBiService.saveThietBi(thietbi);
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

    @GetMapping("/deleteMultiple")
    public String deleteMultipleThietBi(@RequestParam String maTB) {
        try {
            Long id = Long.parseLong(maTB);
            List<ThietBi> allMaTBs = thietBiService.getAllThietBi();
            for (ThietBi thietBi : allMaTBs) {
                if (String.valueOf(thietBi.getMaTB()).startsWith(String.valueOf(id))) {
                    thietBiService.deleteThietBiById(thietBi.getMaTB());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/admin/thiet-bi";
    }

    @PostMapping("/add")
    public String addThietBi(@Valid @ModelAttribute("tb") ThietBiRequest tb,
                             BindingResult result,
                             Model model) {
        try {
            if (result.hasErrors() || existsByMaTB(tb.getMaTB().toString())) {
                if (existsByMaTB(tb.getMaTB().toString())) {
                    result.rejectValue("maTB", "error.tb", "Mã thiết bị đã tồn tại");
                }
                List<ThietBi> thietBiList = thietBiService.getAllThietBi();
                model.addAttribute("thietBiList", thietBiList);
                model.addAttribute("showForm", true);
                return "/admin/thietbi/index";
            }
            ThietBi thietBi = new ThietBi();
            thietBi.setMaTB(tb.getMaTB());
            thietBi.setTenTB(tb.getTenTB());
            thietBi.setMoTaTB(tb.getMoTaTB());

            thietBiService.saveThietBi(thietBi);
            model.addAttribute("showForm", false);
        } catch (Exception e) {
            System.out.println("Lỗi khi thêm thiết bị: " + e.getMessage());
        }
        return "redirect:/admin/thiet-bi";
    }

    private boolean existsByMaTB(String maTB) {
        List<ThietBi> thietBiList = thietBiService.getAllThietBi();
        for (ThietBi tb : thietBiList) {
            if (tb.getMaTB().toString().equals(maTB)) {
                return true;
            }
        }
        return false;
    }
}
