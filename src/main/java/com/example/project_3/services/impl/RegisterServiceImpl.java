package com.example.project_3.services.impl;

import com.example.project_3.models.ThanhVien;
import com.example.project_3.repositories.ThanhVienRepository;
import com.example.project_3.services.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private ThanhVienRepository thanhVienRepository;

    @Override
    public ThanhVien register(ThanhVien tv) {
        return thanhVienRepository.save(tv);
    }
}
