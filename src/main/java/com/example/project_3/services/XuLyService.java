package com.example.project_3.services;

import com.example.project_3.models.XuLy;

import java.util.List;
import org.springframework.data.domain.Page;

public interface XuLyService {
    List<XuLy> getAllXuLy();
    XuLy getXuLyById(int maTV);
    XuLy saveXuLy(XuLy tb);
    void deleteXuLyById(int tv);
    public Page<XuLy> getViPhamByMaTV(Long maTV);
}
