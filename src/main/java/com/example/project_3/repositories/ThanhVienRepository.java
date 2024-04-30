package com.example.project_3.repositories;

import com.example.project_3.models.ThanhVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ThanhVienRepository extends JpaRepository<ThanhVien, Long>, JpaSpecificationExecutor<ThanhVien> {
}