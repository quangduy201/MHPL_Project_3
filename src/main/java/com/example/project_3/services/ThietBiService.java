package com.example.project_3.services;

import com.example.project_3.models.ThanhVien;
import com.example.project_3.models.ThietBi;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;

public interface ThietBiService {
    Page<ThietBi> getAllThietBi(Map<String, String> requestParams);
    List<ThietBi> getAllThietBi();
    List<ThietBi> getAllThietBiByType(String type);
    ThietBi getThietBiById(Long maTV);
    ThietBi saveThietBi(ThietBi tb);
    void deleteThietBiById(Long tv);
    public Page<ThietBi> getThietBiDangMuonByMaTV(Long maTV);
    public Page<ThietBi> getThietBiDangDatChoByMaTV(Long maTV);
    
}
