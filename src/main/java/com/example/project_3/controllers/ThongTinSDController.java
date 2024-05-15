package com.example.project_3.controllers;

import com.example.project_3.models.ThanhVien;
import com.example.project_3.models.ThietBi;
import com.example.project_3.models.ThongTinSD;
import com.example.project_3.models.XuLy;
import com.example.project_3.payloads.requests.XuLyRequest;
import com.example.project_3.services.ThanhVienService;
import com.example.project_3.services.ThietBiService;
import com.example.project_3.services.ThongTinSDService;
import com.example.project_3.services.XuLyService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/thong-tin")
public class ThongTinSDController {
    @Autowired
    private ThongTinSDService thongTinSDService;

    @Autowired
    private ThanhVienService thanhVienService;

    @Autowired
    private ThietBiService thietBiService;

    @Autowired
    private XuLyService xuLyService;

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

            if (thanhVienService.getThanhVienById(idTV) == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Không có mã thành viên này");
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
            String tb = thongTinSDService.checkThietBiDaDatCho(idTV, idTB, dateTime);
            if(tb != null) {
                return ResponseEntity.ok(tb);
            } else {
                ThanhVien thanhVien = thanhVienService.getThanhVienById(idTV);
                ThietBi thietBi = thietBiService.getThietBiById(idTB);
                ZoneId zoneId = ZoneId.systemDefault();
                Instant instant = dateTime.atZone(zoneId).toInstant();
                ThongTinSD thongTinSD = ThongTinSD.builder()
                        .maTV(thanhVien)
                        .maTB(thietBi)
                        .trangThai(0)
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

            if (thanhVienService.getThanhVienById(idTV) == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Không có mã thành viên này");
            }

            List<XuLy> xuLyList = xuLyService.getViPhamKhoaTaiKhoanByMaTVAnd(idTV);
            if(!xuLyList.isEmpty()) {
                XuLy xuLy = xuLyList.get(0);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Thành viên này đang vi phạm: " + xuLy.getHinhThucXL());
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime dateTime = LocalDateTime.parse(date, formatter);

            String tb = thongTinSDService.checkThietBiDaDatCho(idTV, idTB, dateTime);
            String tb1 = thongTinSDService.checkThietBiDangDuocMuon(idTV, idTB, dateTime);

            if (tb != null || tb1 != null) {
                return ResponseEntity.ok("Thiết bị này đang đặt chỗ hoặc đang được mượn");
            } else {
                ThanhVien thanhVien = thanhVienService.getThanhVienById(idTV);
                ThietBi thietBi = thietBiService.getThietBiById(idTB);
                ZoneId zoneId = ZoneId.systemDefault();
                Instant instant = dateTime.atZone(zoneId).toInstant();
                ThongTinSD thongTinSD = ThongTinSD.builder()
                        .maTV(thanhVien)
                        .maTB(thietBi)
                        .trangThai(0)
                        .tgMuon(instant)
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
    public ResponseEntity<?> tra(@RequestParam String maTV, @RequestParam String maTB) {
        try {
            Long idTB = Long.parseLong(maTB);
            Long idTV = Long.parseLong(maTV);

            if (thanhVienService.getThanhVienById(idTV) == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Không có mã thành viên này");
            }

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
    public ResponseEntity<?> xoa(@RequestParam String maTV, @RequestParam String maTB) {
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

            ThongTinSD ttsd = thongTinSDService.getThongTinSDById(idTT).orElse(null);

            if (ttsd != null) {
                LocalDateTime dateTime = LocalDateTime.now();

                String tb = thongTinSDService.checkThietBiDangDuocMuon(
                        ttsd.getMaTV().getMaTV(), ttsd.getMaTB().getMaTB(), dateTime
                );

                if (tb != null) {
                    return ResponseEntity.ok("Thiết bị này đang được mượn");
                }

                ThongTinSD thongTinSD = thongTinSDService.getThongTinSDById(idTT).orElse(null);
                if (thongTinSD != null && thongTinSD.getTgMuon() == null) {
                    thongTinSD.setTgMuon(Instant.now());
                    thongTinSD.setTgDatcho(null);
                    thongTinSD.setTrangThai(1);
                    thongTinSDService.saveThongTinSD(thongTinSD);
                    return ResponseEntity.ok("Mượn thành công");
                } else {
                    return ResponseEntity.badRequest().body("Không thể mượn thiết bị");
                }
            }

            return ResponseEntity.badRequest().body("Mã thông tin sử dụng không hợp lệ");
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
                thongTinSD.setTgTra(Instant.now());
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

    @GetMapping("/admin/boithuong")
    public ResponseEntity<?> boithuong(@RequestParam String maTT) {
        try {
            Integer idTT = Integer.parseInt(maTT);
            ThongTinSD thongTinSD = thongTinSDService.getThongTinSDById(idTT).orElse(null);
            if (thongTinSD != null && thongTinSD.getTgMuon() != null && thongTinSD.getTgTra() == null) {
                thongTinSD.setTgTra(Instant.now());
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
            if (thongTinSD != null && thongTinSD.getTgDatcho() == null) {
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
