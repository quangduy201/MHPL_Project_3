package com.example.project_3.services;

import com.example.project_3.models.ThanhVien;
import com.example.project_3.models.ThietBi;
import com.example.project_3.models.ThongTinSD;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ThongTinSDService {
    String checkThietBiDaDatCho(Long maTB, LocalDateTime date);
    ThongTinSD getThongTinSDMuonByID(Long maTV, Long maTB);
    ThongTinSD getThongTinSDDatChoByID(Long maTV, Long maTB);
    Page<ThietBi> showAllMuonThietBi(Map<String, String> requestParams);
    Page<ThietBi> showAllDatThietBi(Map<String, String> requestParams);
    void muonThietBi(Long maTV, Long maTB);
    void deleteThongTinSD(ThongTinSD thongTinSD);
    ThongTinSD saveThongTinSD(ThongTinSD tinSD);
}
