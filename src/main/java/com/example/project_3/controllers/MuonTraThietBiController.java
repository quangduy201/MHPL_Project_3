package com.example.project_3.controllers;

import com.example.project_3.models.ThietBi;
import com.example.project_3.payloads.responses.ThanhVienResponse;
import com.example.project_3.services.ThietBiService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Controller
@RequestMapping("/admin/muon-tra-thiet-bi")
public class MuonTraThietBiController {
    private final ThietBiService thietBiService;

    @Autowired
    public MuonTraThietBiController(ThietBiService thietBiService) {
        this.thietBiService = thietBiService;
    }

    @GetMapping({"", "/"})
    public String index(Model model, @RequestParam Map<String, String> requestParam, HttpSession session) {
        if (session.getAttribute("admin") != null) {
            String dateParam = requestParam.get("date");

            Page<ThietBi> thietBiList = thietBiService.getAllThietBi(requestParam);

            LocalDateTime dateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

            // Parse the date parameter if it's not null or empty
            if (dateParam != null && !dateParam.isEmpty()) {
                dateTime = LocalDateTime.parse(dateParam, formatter);

                if (dateTime.isBefore(LocalDateTime.now())) {
                    dateTime = LocalDateTime.now();
                }
            }

            requestParam.remove("page");
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<String, String> entry: requestParam.entrySet()) {
                builder.append(entry.getKey())
                        .append("=")
                        .append(entry.getValue())
                        .append("&");
            }
            if (!builder.isEmpty()) {
                builder.setLength(builder.length() - 1);
            }
            model.addAttribute("tbList", thietBiList);
            model.addAttribute("params", builder.toString());

            String formattedDateTime = dateTime.format(formatter);

            model.addAttribute("date", formattedDateTime);

            return "/admin/muontrathietbi/index";
        }

        return "redirect:/admin/login";
    }

}
