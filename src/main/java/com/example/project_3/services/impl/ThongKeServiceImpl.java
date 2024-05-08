package com.example.project_3.services.impl;

import com.example.project_3.repositories.ThongTinSDRepository;
import com.example.project_3.services.ThongKeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ThongKeServiceImpl implements ThongKeService {

    @Autowired
    private ThongTinSDRepository thongTinSDRepository;

    @Override
    public List<Object[]> thongKeThanhVienVaoKhuHocTap(LocalDateTime startTime, LocalDateTime endTime, String khoa, String nganh, boolean isTable) {
        if(isTable)
            return thongTinSDRepository.thongKeThanhVienVaoKhuHocTapTable(startTime, endTime, khoa, nganh);
        else
            return thongTinSDRepository.thongKeThanhVienVaoKhuHocTap(startTime, endTime, khoa, nganh);
    }

    @Override
    public List<Object[]> thongKeThietBiDaDuocMuon(LocalDateTime startTime, LocalDateTime endTime, Long maTB, boolean isTable) {
        if(isTable)
            return thongTinSDRepository.thongKeThietBiDaDuocMuonTable(startTime, endTime, maTB);
        else
            return thongTinSDRepository.thongKeThietBiDaDuocMuon(startTime, endTime, maTB);
    }

    @Override
    public List<Object[]> thongKeThietBiDangDuocMuon(LocalDateTime startTime, LocalDateTime endTime, Long maTB, boolean isTable) {
        if(isTable)
            return thongTinSDRepository.thongKeThietBiDangDuocMuonTable(startTime, endTime, maTB);
        else
            return thongTinSDRepository.thongKeThietBiDangDuocMuon(startTime, endTime, maTB);
    }

    @Override
    public List<Object[]> thongKeXuLy(LocalDateTime startTime, LocalDateTime endTime, boolean trangThai, boolean isTable) {
        if(isTable)
            return thongTinSDRepository.thongKeXuLyTable(startTime, endTime, trangThai);
        else
            return thongTinSDRepository.thongKeXuLy(startTime, endTime, trangThai);
    }
}
