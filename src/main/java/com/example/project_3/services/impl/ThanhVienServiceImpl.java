package com.example.project_3.services.impl;

import com.example.project_3.models.ThanhVien;
import com.example.project_3.repositories.ThanhVienRepository;
import com.example.project_3.services.ThanhVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThanhVienServiceImpl implements ThanhVienService {
    private final ThanhVienRepository thanhVienRepository;

    @Autowired
    public ThanhVienServiceImpl(ThanhVienRepository thanhVienRepository) {
        this.thanhVienRepository = thanhVienRepository;
    }

    @Override
    public List<ThanhVien> getAllThanhVien() {
        return thanhVienRepository.findAll();
    }

    @Override
    public List<String> getKhoaList() {
        return thanhVienRepository.findAll().stream().map(ThanhVien::getKhoa).toList();
    }

    @Override
    public List<String> getNganhList() {
        return thanhVienRepository.findAll().stream().map(ThanhVien::getNganh).toList();
    }

    @Override
    public ThanhVien getThanhVienById(Long maTV) {
        return thanhVienRepository.findById(maTV).orElse(null);
    }

    @Override
    public ThanhVien saveThanhVien(ThanhVien tv) {
        return thanhVienRepository.save(tv);
    }

    @Override
    public void deleteThanhVienById(Long tv) {
        thanhVienRepository.deleteById(tv);
    }
}
