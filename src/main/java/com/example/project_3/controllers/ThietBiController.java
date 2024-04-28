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
                                @Valid @ModelAttribute("tbRequest") ThietBiRequest tbRequest,
                                BindingResult result) {
        try {
            Long id = Long.parseLong(maTB);
            ThietBi tb = thietBiService.getThietBiById(id);
            model.addAttribute("thietBi", tb);

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
    public String addThietBi(@Valid @ModelAttribute("thietBiRequest") ThietBiRequest thietBiRequest,
                             BindingResult result) {
        try {
            ThietBi thietBi = new ThietBi();
            thietBi.setMaTB(thietBiRequest.getMaTB());
            thietBi.setTenTB(thietBiRequest.getTenTB());
            thietBi.setMoTaTB(thietBiRequest.getMoTaTB());

            thietBiService.saveThietBi(thietBi);

        } catch (Exception e) {
            System.out.println("Lỗi khi thêm thiết bị: " + e.getMessage());
        }
        return "redirect:/admin/thiet-bi";
    }
}
