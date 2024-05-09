package com.example.project_3.repositories;

import com.example.project_3.models.ThietBi;
import com.example.project_3.models.ThongTinSD;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThongTinSDRepository extends JpaRepository<ThongTinSD, Integer> {
    @Query("SELECT tt.maTB FROM ThongTinSD tt WHERE tt.maTB.maTB = ?1 AND tt.tgDatcho IS NOT NULL ")
    List<ThietBi> findThietBiByMaTBEqualsAndTgDatchoNotNull(Long maTB);

    @Query("SELECT tt.maTB FROM ThongTinSD tt WHERE tt.maTB.maTB = ?1 AND tt.tgMuon IS NOT NULL AND tt.tgTra IS NULL ")
    List<ThietBi> findThietBiByMaTBEqualsAndTgMuonNotNullAndTgTraNull(Long maTB);

    @Query("SELECT tt.maTB FROM ThongTinSD tt WHERE tt.tgMuon IS NOT NULL AND tt.tgTra IS NULL")
    Page<ThietBi> findThietBiByTgMuonNotNullAndTgTraNull(Pageable pageable);

    @Query("SELECT tt.maTB FROM ThongTinSD tt WHERE tt.tgDatcho IS NOT NULL ")
    Page<ThietBi> findThietBiByTgDatchoNotNull(Pageable pageable);

    @Query("SELECT tt FROM ThongTinSD tt WHERE tt.maTV.maTV = ?1 AND tt.maTB.maTB = ?2 AND tt.tgMuon IS NOT NULL AND tt.tgTra IS NULL ")
    List<ThongTinSD> findThongTinSDByMaTBEqualsAndTgMuonNotNullAndTgTraNull(Long maTV, Long maTB);

}