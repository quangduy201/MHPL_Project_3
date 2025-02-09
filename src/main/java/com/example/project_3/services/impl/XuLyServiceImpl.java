package com.example.project_3.services.impl;

import com.example.project_3.models.ThietBi;
import com.example.project_3.models.XuLy;
import com.example.project_3.repositories.ThietBiRepository;
import com.example.project_3.repositories.XuLyRepository;
import com.example.project_3.services.ThietBiService;
import com.example.project_3.services.XuLyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.data.domain.Page;

@Service
public class XuLyServiceImpl implements XuLyService {
    @Autowired
    private XuLyRepository xuLyRepository;

    @Override
    public List<XuLy> getAllXuLy() {
        return xuLyRepository.findAll();
    }

    @Override
    public XuLy getXuLyById(int maXL) {
        return xuLyRepository.findById(maXL).orElse(null);
    }

    @Override
    public XuLy saveXuLy(XuLy xl) {
        return xuLyRepository.save(xl);
    }

    @Override
    public void deleteXuLyById(int tv) {
        xuLyRepository.deleteById(tv);
    }

    @Override
    public Page<XuLy> getViPhamByMaTV(Long maTV) {
        return xuLyRepository.findViPhamByMaTVEquals(maTV,null);
    }

    @Override
    public List<XuLy> getViPhamKhoaTaiKhoanByMaTVAnd(Long maTV) {
        return xuLyRepository.findViPhamKhoaTaiKhoanByMaTVAnd(maTV);
    }

    @Override
    public List<XuLy> getViPhamKhoaTaiKhoanByMaTVOr(Long maTV) {
        return xuLyRepository.findViPhamKhoaTaiKhoanByMaTVOr(maTV);
    }

}
