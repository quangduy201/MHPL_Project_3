package com.example.project_3.repositories;

import com.example.project_3.models.XuLy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface XuLyRepository extends JpaRepository<XuLy, Integer> {

    @Query("SELECT xl FROM XuLy xl WHERE xl.maTV.maTV = ?1")
    Page<XuLy> findViPhamByMaTVEquals(Long maTV, Pageable pageable);
}
