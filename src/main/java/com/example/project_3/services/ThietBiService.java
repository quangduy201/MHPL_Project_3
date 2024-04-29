package com.example.project_3.services;

import com.example.project_3.models.ThanhVien;
import com.example.project_3.models.ThietBi;

import java.util.List;

public interface ThietBiService {
    List<ThietBi> getAllThietBi();
    ThietBi getThietBiById(Long maTV);
    ThietBi saveThietBi(ThietBi tb);
    void deleteThietBiById(Long tv);
}
