package com.example.project_3.services.impl;

import com.example.project_3.models.ThanhVien;
import com.example.project_3.models.ThietBi;
import com.example.project_3.repositories.ThanhVienRepository;
import com.example.project_3.repositories.ThietBiRepository;
import com.example.project_3.repositories.ThongTinSDRepository;
import com.example.project_3.services.ThanhVienService;
import com.example.project_3.services.ThietBiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThietBiServiceImpl implements ThietBiService {
    @Autowired
    private ThietBiRepository thietBiRepository;

    @Override
    public List<ThietBi> getAllThietBi() {
        return thietBiRepository.findAll();
    }

    @Override
    public ThietBi getThietBiById(Long maTB) {
        return thietBiRepository.findById(maTB).orElse(null);
    }

    public Page<ThietBi> getThietBiDangMuonByMaTV(Long maTV) {
        return thietBiRepository.findThietBiByMaTVEqualsAndTgMuonIsNotNullAndTgTraIsNull(maTV, null);
    }

    @Override
    public ThietBi saveThietBi(ThietBi tv) {
        return thietBiRepository.save(tv);
    }

    @Override
    public void deleteThietBiById(Long tv) {
        thietBiRepository.deleteById(tv);
    }
}
