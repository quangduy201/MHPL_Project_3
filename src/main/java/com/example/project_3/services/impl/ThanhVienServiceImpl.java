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

import java.util.Map;

@Service
public class ThanhVienServiceImpl implements ThanhVienService {
    private final ThanhVienRepository thanhVienRepository;

    @Autowired
    public ThanhVienServiceImpl(ThanhVienRepository thanhVienRepository) {
        this.thanhVienRepository = thanhVienRepository;
    }

    @Override
    public Page<ThanhVien> getThanhVien(Map<String, String> requestParams) {
        // maTV LIKE '%...%'
        // [AND hoTen LIKE '%...%' ]
        // [AND khoa LIKE '%...%' ]
        // [AND nganh LIKE '%...%' ]
        // [AND sdt LIKE '%...%' ]
        // [AND email like '%...%' ]
        Specification<ThanhVien> specification = BaseSpecification.buildLikeSpecification(requestParams, true);

        String page = requestParams.get("page");
        Pageable pageable = Pageable.ofSize(3).withPage(0);
        if (page != null && page.trim().matches("^\\d+$"))
            pageable = pageable.withPage(Integer.parseInt(page) - 1);

        return thanhVienRepository.findAll(specification, pageable);
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
