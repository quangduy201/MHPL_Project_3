package com.example.project_3.repositories;

import com.example.project_3.models.ThongTinSD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThongTinSDRepository extends JpaRepository<ThongTinSD, Integer> {
}