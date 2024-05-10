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
import org.springframework.web.bind.annotation.*;

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

            String tb = thongTinSDService.checkThietBiDaDatCho(idTB, dateTime);

            ZoneOffset zoneOffset = ZoneOffset.ofHours(0);

            if(tb != null) {
                return ResponseEntity.badRequest().body(tb);
            } else {
                ThanhVien thanhVien = thanhVienService.getThanhVienById(idTV);
                ThietBi thietBi = thietBiService.getThietBiById(idTB);

                Instant instant = dateTime.toInstant(zoneOffset);

                ThongTinSD thongTinSD = ThongTinSD.builder()
                        .maTV(thanhVien)
                        .maTB(thietBi)
                        .tgDatcho(instant)
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

            // Define the DateTimeFormatter for the expected date format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

            LocalDateTime dateTime = LocalDateTime.parse(date, formatter);

            String tb = thongTinSDService.checkThietBiDaDatCho(idTB, dateTime);

            ZoneOffset zoneOffset = ZoneOffset.ofHours(0);

            if (tb != null) {
                return ResponseEntity.badRequest().body(tb);
            } else {
                ThanhVien thanhVien = thanhVienService.getThanhVienById(idTV);
                ThietBi thietBi = thietBiService.getThietBiById(idTB);

                // If date is null, set the current time
                Instant muonInstant = dateTime.toInstant(zoneOffset);

                // Build ThongTinSD object
                ThongTinSD thongTinSD = ThongTinSD.builder()
                        .maTV(thanhVien)
                        .maTB(thietBi)
                        .tgMuon(muonInstant)
                        .build();

                // Save ThongTinSD object
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

    @GetMapping("/admin/muon")
    public ResponseEntity<?> muon(@RequestParam String maTT) {
        try {
            Integer idTT = Integer.parseInt(maTT);
            ThongTinSD thongTinSD = thongTinSDService.getThongTinSDById(idTT).orElse(null);
            if (thongTinSD != null && thongTinSD.getTgMuon() == null) {
                Instant muonInstant = Instant.now();
                thongTinSD.setTgMuon(muonInstant);
                thongTinSDService.saveThongTinSD(thongTinSD);
                return ResponseEntity.ok("Mượn thành công");
            } else {
                return ResponseEntity.badRequest().body("Không thể mượn thiết bị");
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Mã thiết bị không hợp lệ");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Server Error: " + e.getMessage());
        }
    }


    @GetMapping("/admin/tra")
    public ResponseEntity<?> tra(@RequestParam String maTT) {
        try {
            Integer idTT = Integer.parseInt(maTT);
            ThongTinSD thongTinSD = thongTinSDService.getThongTinSDById(idTT).orElse(null);
            if (thongTinSD != null && thongTinSD.getTgMuon() != null && thongTinSD.getTgTra() == null) {
                Instant traInstant = Instant.now();
                thongTinSD.setTgTra(traInstant);
                thongTinSDService.saveThongTinSD(thongTinSD);
                return ResponseEntity.ok("Trả thành công");
            } else {
                return ResponseEntity.badRequest().body("Không thể trả thiết bị");
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Mã thiết bị không hợp lệ");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error: " + e.getMessage());
        }
    }

    @GetMapping("/admin/huy")
    public ResponseEntity<?> huy(@RequestParam String maTT) {
        try {
            Integer idTT = Integer.parseInt(maTT);
            ThongTinSD thongTinSD = thongTinSDService.getThongTinSDById(idTT).orElse(null);
            if (thongTinSD != null && thongTinSD.getTgMuon() == null) {
                thongTinSDService.deleteThongTinSD(thongTinSD);
                return ResponseEntity.ok("Hủy đặt chỗ thành công");
            } else {
                return ResponseEntity.badRequest().body("Không thể hủy đặt chỗ thiết bị");
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Mã thiết bị không hợp lệ");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error: " + e.getMessage());
        }
    }

    @PostMapping("/excel")
    public String excel(@RequestBody String[][] rows) {
        try {
            for (String[] row : rows) {
                ThongTinSD tt = ThongTinSD.builder()
                        .maTT(Integer.parseInt(row[0]))
                        .maTV(thanhVienService.getThanhVienById(Long.parseLong(row[1])))
                        .maTB(thietBiService.getThietBiById(Long.parseLong(row[2])))
                        .tgVao(Instant.parse(row[3]))
                        .tgMuon(Instant.parse(row[4]))
                        .tgTra(Instant.parse(row[5]))
                        .tgDatcho(Instant.parse(row[6]))
                        .build();
                thongTinSDService.saveThongTinSD(tt);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/admin/thanh-vien";
    }
}
