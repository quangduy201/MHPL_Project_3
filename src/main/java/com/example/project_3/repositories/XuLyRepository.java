package com.example.project_3.repositories;

import com.example.project_3.models.XuLy;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface XuLyRepository extends JpaRepository<XuLy, Integer> {
    @Query("SELECT x FROM XuLy x WHERE x.maTV.maTV = ?1")
    List<XuLy> findByMaTV(Long maTV);
}