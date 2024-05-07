/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.project_3.services.impl;

import com.example.project_3.repositories.ThongTinSDRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Dll
 */
public class ThongTinSDServiceImpl {
    private final ThongTinSDRepository thongtinSDRepository;
    @Autowired
    public ThongTinSDServiceImpl(ThongTinSDRepository thongtinSDRepository) {
        this.thongtinSDRepository = thongtinSDRepository;
       
    }
    
}
