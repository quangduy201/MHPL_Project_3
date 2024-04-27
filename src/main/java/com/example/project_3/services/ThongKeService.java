package com.example.project_3.services;

import com.example.project_3.models.ThanhVien;

import java.time.LocalDateTime;
import java.util.List;

public interface ThongKeService {
    List<Object[]> thongKeThanhVienVaoKhuHocTap(LocalDateTime startTime, LocalDateTime endTime, String khoa, String nganh, boolean isTable);
}
