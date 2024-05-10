package com.example.project_3.services;

import com.example.project_3.models.ThongTinSD;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ThongTinSDService {
    String checkThietBiDaDatCho(Long maTB, LocalDateTime date);
    ThongTinSD getThongTinSDMuonByID(Long maTV, Long maTB);
    ThongTinSD getThongTinSDDatChoByID(Long maTV, Long maTB);
    Page<ThongTinSD> showAllMuonThietBi(Map<String, String> requestParams, Long maTV);
    Page<ThongTinSD> showAllDatThietBi(Map<String, String> requestParams, Long maTV);
    void muonThietBi(Long maTV, Long maTB);
    void deleteThongTinSD(ThongTinSD thongTinSD);
    ThongTinSD saveThongTinSD(ThongTinSD tinSD);
    List<ThongTinSD> getAllThongTinSD();

    Optional<ThongTinSD> getThongTinSDById(Integer maTT);
}
