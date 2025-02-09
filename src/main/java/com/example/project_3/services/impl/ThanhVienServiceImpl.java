package com.example.project_3.services.impl;

import com.example.project_3.models.ThanhVien;
import com.example.project_3.repositories.ThanhVienRepository;
import com.example.project_3.services.ThanhVienService;
import com.example.project_3.specifications.BaseSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ThanhVienServiceImpl implements ThanhVienService {
    private final ThanhVienRepository thanhVienRepository;

    @Autowired
    public ThanhVienServiceImpl(ThanhVienRepository thanhVienRepository) {
        this.thanhVienRepository = thanhVienRepository;
    }

    @Override
    public List<ThanhVien> getAllThanhVien() {
        List<ThanhVien> allThanhVien = thanhVienRepository.findAll();
        List<ThanhVien> thanhVienLoai0 = new ArrayList<>();

        for (ThanhVien thanhVien : allThanhVien) {
            if (thanhVien.getMaTV() != 0) {
                thanhVienLoai0.add(thanhVien);
            }
        }

        return thanhVienLoai0;
    }


    @Override
    public Page<ThanhVien> getThanhVien(Map<String, String> requestParams) {
        Specification<ThanhVien> specification;
        if (requestParams.containsKey("all")) {
            String value = requestParams.get("all");
            Map<String, String> params = Map.of(
                    "maTV", value,
                    "hoTen", value,
                    "khoa", value,
                    "nganh", value,
                    "sdt", value,
                    "email", value
            );
            specification = BaseSpecification.buildLikeSpecification(params, false);
        } else {
            // maTV LIKE '%...%'
            // [AND hoTen LIKE '%...%' ]
            // [AND khoa LIKE '%...%' ]
            // [AND nganh LIKE '%...%' ]
            // [AND sdt LIKE '%...%' ]
            // [AND email like '%...%' ]
            specification = BaseSpecification.buildLikeSpecification(requestParams, true);
        }

        String page = requestParams.get("page");
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        if (page != null && page.trim().matches("^\\d+$"))
            pageable = pageable.withPage(Integer.parseInt(page) - 1);

        return thanhVienRepository.findAll(specification, pageable);
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
    public ThanhVien getThanhVienBySdt(String sdt) {
        return thanhVienRepository.findBySdt(sdt).orElse(null);
    }

    @Override
    public ThanhVien getThanhVienByEmail(String email) {
        return thanhVienRepository.findByEmail(email).orElse(null);
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
