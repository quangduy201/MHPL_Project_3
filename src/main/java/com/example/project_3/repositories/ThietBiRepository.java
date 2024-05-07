package com.example.project_3.repositories;

import com.example.project_3.models.ThietBi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ThietBiRepository extends JpaRepository<ThietBi, Long> {
    @Query("SELECT tt.maTB FROM ThongTinSD tt WHERE tt.maTV = ?1 AND tt.tgMuon IS NOT NULL AND tt.tgTra IS NULL")
    Page<ThietBi> findThietBiByMaTVEqualsAndTgMuonIsNotNullAndTgTraIsNull(Long maTV, Pageable pageable);

    @Query("SELECT tt.maTB FROM ThongTinSD tt WHERE tt.maTV = ?1 AND tt.tgDatcho IS NOT NULL")
    Page<ThietBi> findThietBiByMaTVEqualsAndTgDatchoIsNotNull(Long maTV, Pageable pageable);
}