package com.example.project_3.services;

import com.example.project_3.models.ThanhVien;

import java.time.LocalDateTime;
import java.util.List;

public interface ThongKeService {
    List<Object[]> thongKeThanhVienVaoKhuHocTap(LocalDateTime startTime, LocalDateTime endTime, String khoa, String nganh, boolean isTable);
    List<Object[]> thongKeThietBiDaDuocMuon(LocalDateTime startTime, LocalDateTime endTime, Long maTB, boolean isTable);
    List<Object[]> thongKeThietBiDangDuocMuon(LocalDateTime startTime, LocalDateTime endTime, Long maTB, boolean isTable);
    List<Object[]> thongKeXuLy(LocalDateTime startTime, LocalDateTime endTime, boolean trangThai, boolean isTable);
}
