package com.example.project_3.services.impl;

import com.example.project_3.models.ThietBi;
import com.example.project_3.models.ThongTinSD;
import com.example.project_3.repositories.ThongTinSDRepository;
import com.example.project_3.services.ThongTinSDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ThongTinSDServiceImpl implements ThongTinSDService {
    private final ThongTinSDRepository thongTinSDRepository;

    @Autowired
    public ThongTinSDServiceImpl(ThongTinSDRepository thongTinSDRepository) {
        this.thongTinSDRepository = thongTinSDRepository;
    }


    @Override
    public String checkThietBiDaDatCho(Long maTB) {
        List<ThietBi> thietBiDatCho = thongTinSDRepository.findThietBiByMaTBEqualsAndTgDatchoNotNull(maTB);
        List<ThietBi> thietBiMuon = thongTinSDRepository.findThietBiByMaTBEqualsAndTgMuonNotNullAndTgTraNull(maTB);
        if(!thietBiDatCho.isEmpty()) {
            return "Thiết bị này đã được đặt chỗ";
        } else if(!thietBiMuon.isEmpty()) {
            return "Thiết bị này đã được muợn";
        }
        return null;
    }

    @Override
    public ThongTinSD getThongTinSDMuonByID(Long maTV, Long maTB) {
        List<ThongTinSD> thietBiMuon = thongTinSDRepository.findThongTinSDByMaTBEqualsAndTgMuonNotNullAndTgTraNull(maTV,maTB);
        if(thietBiMuon.isEmpty()) {
            return null;
        }
        return thietBiMuon.get(0);
    }

    @Override
    public ThongTinSD getThongTinSDDatChoByID(Long maTV, Long maTB) {
        List<ThongTinSD> thietBiDatCho = thongTinSDRepository.findThongTinSDByMaTBEqualsAndTgDatchoNotNull(maTV,maTB);
        if(thietBiDatCho.isEmpty()) {
            return null;
        }
        return thietBiDatCho.get(0);
    }

    @Override
    public void muonThietBi(Long maTV, Long maTB) {

    }

    @Override
    public void deleteThongTinSD(ThongTinSD thongTinSD) {
        thongTinSDRepository.delete(thongTinSD);
    }


    @Override
    public Page<ThongTinSD> showAllMuonThietBi(Map<String, String> requestParams, Long maTV) {
        String page = requestParams.get("page");
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        if (page != null && page.trim().matches("^\\d+$"))
            pageable = pageable.withPage(Integer.parseInt(page) - 1);
        return thongTinSDRepository.findThietBiByMaTVEqualsANDTgMuonNotNullAndTgTraNull(pageable, maTV);
    }

    @Override
    public Page<ThongTinSD> showAllDatThietBi(Map<String, String> requestParams, Long maTV) {
        String page = requestParams.get("page");
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        if (page != null && page.trim().matches("^\\d+$"))
            pageable = pageable.withPage(Integer.parseInt(page) - 1);
        return thongTinSDRepository.findThietBiByMaTVEqualsTgDatchoNotNull(pageable, maTV);
    }


    @Override
    public ThongTinSD saveThongTinSD(ThongTinSD tinSD) {
        return thongTinSDRepository.save(tinSD);
    }
}