package com.example.project_3.services;

import com.example.project_3.models.ThanhVien;
import com.example.project_3.models.ThietBi;
import com.example.project_3.models.ThongTinSD;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ThongTinSDService {
    String checkThietBiDaDatCho(Long maTB);
    ThongTinSD getThietBiByID(Long maTV, Long maTB);
    Page<ThietBi> showAllMuonThietBi(Map<String, String> requestParams);
    Page<ThietBi> showAllDatThietBi(Map<String, String> requestParams);
    void muonThietBi(Long maTV, Long maTB);
    ThongTinSD saveThongTinSD(ThongTinSD tinSD);
}
