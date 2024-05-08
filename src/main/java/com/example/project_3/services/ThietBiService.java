package com.example.project_3.services;

import com.example.project_3.models.ThanhVien;
import com.example.project_3.models.ThietBi;

import java.util.List;
import org.springframework.data.domain.Page;

public interface ThietBiService {
    List<ThietBi> getAllThietBi();
    ThietBi getThietBiById(Long maTV);
    ThietBi saveThietBi(ThietBi tb);
    void deleteThietBiById(Long tv);
    public Page<ThietBi> getThietBiDangMuonByMaTV(Long maTV);
    public Page<ThietBi> getThietBiDangDatChoByMaTV(Long maTV);
    
}
