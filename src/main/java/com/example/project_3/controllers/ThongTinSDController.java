package com.example.project_3.controllers;

import com.example.project_3.models.ThanhVien;
import com.example.project_3.models.ThietBi;
import com.example.project_3.models.ThongTinSD;
import com.example.project_3.services.ThanhVienService;
import com.example.project_3.services.ThietBiService;
import com.example.project_3.services.ThongTinSDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/thong-tin")
public class ThongTinSDController {
    @Autowired
    private ThongTinSDService thongTinSDService;

    @Autowired
    private ThanhVienService thanhVienService;

    @Autowired
    private ThietBiService thietBiService;

    public String getTime() {
        ZoneId vietnamZoneId = ZoneId.of("Asia/Ho_Chi_Minh");
        ZonedDateTime zdt = ZonedDateTime.now(vietnamZoneId);
        ZonedDateTime zdtPlusSevenHours = zdt.plusHours(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss'Z'");
        return zdtPlusSevenHours.format(formatter);
    }
    @GetMapping("/dat-cho")
    public ResponseEntity<?> datCho(@RequestParam String maTV,
                                    @RequestParam String maTB,
                                    @RequestParam String date) {
        try {
            Long idTB = Long.parseLong(maTB);
            Long idTV = Long.parseLong(maTV);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
            String tb = thongTinSDService.checkThietBiDaDatCho(idTV, idTB, dateTime);
            if(tb != null) {
                return ResponseEntity.ok(tb);
            } else {
                ThanhVien thanhVien = thanhVienService.getThanhVienById(idTV);
                ThietBi thietBi = thietBiService.getThietBiById(idTB);
                ThongTinSD thongTinSD = ThongTinSD.builder()
                        .maTV(thanhVien)
                        .maTB(thietBi)
                        .tgDatcho(Instant.parse(getTime()))
                        .build();
                thongTinSDService.saveThongTinSD(thongTinSD);
                return ResponseEntity.ok("Đặt chỗ thành công");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error: " + e.getMessage());
        }
    }


    @GetMapping("/muon")
    public ResponseEntity<?> muon(@RequestParam String maTV,
                                  @RequestParam String maTB,
                                  @RequestParam String date) {
        try {
            Long idTB = Long.parseLong(maTB);
            Long idTV = Long.parseLong(maTV);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
            String tb = thongTinSDService.checkThietBiDaDatCho(idTV, idTB, dateTime);
            if (tb != null) {
                return ResponseEntity.ok(tb);
            } else {
                ThanhVien thanhVien = thanhVienService.getThanhVienById(idTV);
                ThietBi thietBi = thietBiService.getThietBiById(idTB);
                ThongTinSD thongTinSD = ThongTinSD.builder()
                        .maTV(thanhVien)
                        .maTB(thietBi)
                        .tgMuon(Instant.parse(getTime()))
                        .build();

                thongTinSDService.saveThongTinSD(thongTinSD);
                return ResponseEntity.ok("Mượn thành công");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Server Error: " + e.getMessage());
        }
    }


    @GetMapping("/tra")
    public ResponseEntity<?>  tra(@RequestParam String maTV, @RequestParam String maTB) {
        try {
            Long idTB = Long.parseLong(maTB);
            Long idTV = Long.parseLong(maTV);
            ThongTinSD thongTinSD = thongTinSDService.getThongTinSDMuonByID(idTV, idTB);
            if(thongTinSD != null) {
                thongTinSD.setTgTra(Instant.parse(getTime()));
                thongTinSDService.saveThongTinSD(thongTinSD);
                return null;
            } else {
                return ResponseEntity.badRequest().body("Không thể trả thiết bị");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error: " + e.getMessage());
        }
    }

    @GetMapping("/xoa")
    public ResponseEntity<?>  xoa(@RequestParam String maTV, @RequestParam String maTB) {
        try {
            Long idTB = Long.parseLong(maTB);
            Long idTV = Long.parseLong(maTV);
            ThongTinSD thongTinSD = thongTinSDService.getThongTinSDDatChoByID(idTV, idTB);
            if(thongTinSD != null) {
                thongTinSDService.deleteThongTinSD(thongTinSD);
                return null;
            } else {
                return ResponseEntity.badRequest().body("Không thể hủy đặt chỗ thiết bị");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error: " + e.getMessage());
        }
    }
}
