package com.example.project_3.repositories;

import com.example.project_3.models.ThietBi;
import com.example.project_3.models.ThongTinSD;
import com.example.project_3.models.XuLy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ThongTinSDRepository extends JpaRepository<ThongTinSD, Integer> {
    @Query("SELECT tt.maTB FROM ThongTinSD tt WHERE tt.maTB.maTB = ?1 AND tt.tgDatcho IS NOT NULL AND FUNCTION('DATE', tt.tgDatcho) = ?2")
    List<ThietBi> findThietBiByMaTBEqualsAndTgDatchoNotNull(Long maTB, LocalDate date);

    @Query("SELECT tt.maTB FROM ThongTinSD tt WHERE tt.maTB IS NOT NULL AND tt.tgDatcho IS NULL AND tt.tgTra IS NULL AND tt.tgMuon IS NOT NULL")
    List<ThietBi> findThietBiDangMuon();

    @Query("SELECT tt.maTB FROM ThongTinSD tt WHERE tt.maTB IS NOT NULL AND tt.tgDatcho IS NOT NULL AND tt.tgTra IS NULL AND tt.tgMuon IS NULL")
    List<ThietBi> findThietBiDangDatCho();

    @Query("SELECT tt.maTB FROM ThongTinSD tt WHERE tt.maTB.maTB = ?1 AND tt.tgMuon IS NOT NULL AND tt.tgTra IS NULL AND FUNCTION('DATE', tt.tgMuon) = ?2")
    List<ThietBi> findThietBiByMaTBEqualsAndTgMuonNotNullAndTgTraNull(Long maTB, LocalDate date);

    @Query("SELECT tt FROM XuLy tt WHERE tt.maTV.maTV = ?1 AND tt.trangThaiXL = 0")
    List<XuLy> findXuLyByMaTVEqualsAndTrangThaiXuLyEquals(Long maTV);

    @Query("SELECT tt FROM ThongTinSD tt WHERE tt.maTB.maTB IS NOT NULL AND tt.tgDatcho IS NOT NULL")
    List<ThongTinSD> findTgDatchoNotNull();

    @Query("SELECT tt.maTB FROM ThongTinSD tt WHERE tt.tgMuon IS NOT NULL AND tt.tgTra IS NULL")
    Page<ThietBi> findThietBiByTgMuonNotNullAndTgTraNull(Pageable pageable);

    @Query("SELECT tt FROM ThongTinSD tt WHERE tt.maTV.maTV = ?1 AND tt.tgMuon IS NOT NULL AND tt.tgTra IS NULL")
    Page<ThongTinSD> findThietBiByMaTVEqualsANDTgMuonNotNullAndTgTraNull(Pageable pageable, Long maTV);

    @Query("SELECT tt FROM ThongTinSD tt WHERE tt.maTV.maTV = ?1 AND tt.tgDatcho IS NOT NULL ")
    Page<ThongTinSD> findThietBiByMaTVEqualsTgDatchoNotNull(Pageable pageable, Long maTV);

    @Query("SELECT tt FROM ThongTinSD tt WHERE tt.tgDatcho IS NOT NULL OR tt.tgMuon IS NOT NULL AND tt.maTV.maTV = ?1")
    Page<ThongTinSD> findThongTinSDByMaTVEquals(Pageable pageable, Long maTV);

    @Query("SELECT tt FROM ThongTinSD tt WHERE tt.maTV.maTV = ?1 AND tt.maTB.maTB = ?2 AND tt.tgMuon IS NOT NULL AND tt.tgTra IS NULL ")
    List<ThongTinSD> findThongTinSDByMaTBEqualsAndTgMuonNotNullAndTgTraNull(Long maTV, Long maTB);

    @Query("SELECT tt FROM ThongTinSD tt WHERE tt.maTV.maTV = ?1 AND tt.maTB.maTB = ?2 AND tt.tgDatcho IS NOT NULL ")
    List<ThongTinSD> findThongTinSDByMaTBEqualsAndTgDatchoNotNull(Long maTV, Long maTB);

    @Query("SELECT tt FROM ThongTinSD tt WHERE tt.maTV IS NOT NULL AND tt.maTB IS NULL AND tt.tgVao IS NOT NULL AND tt.tgMuon IS NULL AND tt.tgDatcho IS NULL AND tt.tgTra IS NULL ")
    List<ThongTinSD> findAllThongTinSDOnlyTGVao();

    @Query("SELECT tt FROM ThongTinSD tt WHERE tt.maTV IS NOT NULL AND tt.maTB IS NOT NULL AND tt.tgVao IS NULL AND tt.tgMuon IS NOT NULL AND tt.tgDatcho IS NULL AND tt.tgTra IS NOT NULL ")
    List<ThongTinSD> findAllThongTinSDOnlyThietBiDaMuon();

    @Query("SELECT tt FROM ThongTinSD tt WHERE tt.maTV IS NOT NULL AND tt.maTB IS NOT NULL AND tt.tgVao IS NULL AND tt.tgMuon IS NOT NULL AND tt.tgDatcho IS NULL AND tt.tgTra IS NULL ")
    List<ThongTinSD> findAllThongTinSDOnlyThietBiDangMuon();

    @Query("SELECT tt.maTB FROM ThongTinSD tt WHERE tt.maTB IS NOT NULL AND ((tt.tgDatcho BETWEEN ?1 AND ?2))")
    List<ThietBi> findThietBiByDate(Instant date1, Instant date2);

    void deleteByTgDatchoBefore(Instant date);
}
