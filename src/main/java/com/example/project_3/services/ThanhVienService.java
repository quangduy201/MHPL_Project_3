package com.example.project_3.services;

import com.example.project_3.models.ThanhVien;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface ThanhVienService {
    Page<ThanhVien> getThanhVien(Map<String, String> requestParams);
    ThanhVien getThanhVienById(Long maTV);
    ThanhVien saveThanhVien(ThanhVien tv);
    void deleteThanhVienById(Long tv);
}
