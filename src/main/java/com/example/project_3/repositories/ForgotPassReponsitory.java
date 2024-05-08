/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.project_3.repositories;

import com.example.project_3.models.ThanhVien;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author vithi
 */
public interface ForgotPassReponsitory extends CrudRepository<ThanhVien,Integer>{
    @Query("SELECT c FROM ThanhVien c WHERE c.email = ?1")
    public ThanhVien findByEmail(String email);

    public ThanhVien findByResetPasswordToken(String token);
}
