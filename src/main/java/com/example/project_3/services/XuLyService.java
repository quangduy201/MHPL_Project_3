package com.example.project_3.services;

import com.example.project_3.models.XuLy;

import java.util.List;

public interface XuLyService {
    List<XuLy> getAllXuLy();
    XuLy getXuLyById(int maTV);
    XuLy saveXuLy(XuLy tb);
    void deleteXuLyById(int tv);
}
