package com.example.project_3.services;

import com.example.project_3.models.ThanhVien;

import java.util.List;

public interface ThanhVienService {
    List<ThanhVien> getAllThanhVien();
    ThanhVien getThanhVienById(Long maTV);
    ThanhVien saveThanhVien(ThanhVien tv);
    void deleteThanhVienById(Long tv);
}
