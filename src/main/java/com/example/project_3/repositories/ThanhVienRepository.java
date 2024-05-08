package com.example.project_3.repositories;

import com.example.project_3.models.ThanhVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ThanhVienRepository extends JpaRepository<ThanhVien, Long>, JpaSpecificationExecutor<ThanhVien> {
    Optional<ThanhVien> findByEmail(String email);
    Optional<ThanhVien> findBySdt(String sdt);
}