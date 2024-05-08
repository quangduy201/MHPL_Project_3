package com.example.project_3.repositories;

import com.example.project_3.models.ThongTinSD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ThongTinSDRepository extends JpaRepository<ThongTinSD, Integer> {

    @Query("SELECT FUNCTION('DATE_FORMAT', t.tgVao, '%d-%m-%Y %H:%i:%s') " +
            "FROM ThongTinSD t WHERE t.tgVao BETWEEN ?1 AND ?2 " +
            "AND (?3 IS NULL OR ?3 = 'tatca' OR t.maTV.khoa = ?3) " +
            "AND (?4 IS NULL OR ?4 = 'tatca' OR t.maTV.nganh = ?4)" +
            "GROUP BY FUNCTION('DATE_FORMAT', t.tgVao, '%d-%m-%Y %H:%i:%s')" +
            "ORDER BY FUNCTION('DATE_FORMAT', t.tgVao, '%d-%m-%Y %H:%i:%s')")
    List<Object[]> thongKeThanhVienVaoKhuHocTap(LocalDateTime startTime, LocalDateTime endTime, String khoa, String nganh);

    @Query("SELECT FUNCTION('DATE_FORMAT', t.tgMuon, '%d-%m-%Y %H:%i:%s') " +
            "FROM ThongTinSD t WHERE t.tgMuon BETWEEN ?1 AND ?2 " +
            "AND t.tgTra IS NOT NULL AND (?3 IS NULL OR ?3 = 'tatca' OR t.maTB.maTB = CAST(?3 AS Long))" +
            "GROUP BY FUNCTION('DATE_FORMAT', t.tgMuon, '%d-%m-%Y %H:%i:%s')" +
            "ORDER BY FUNCTION('DATE_FORMAT', t.tgMuon, '%d-%m-%Y %H:%i:%s')")
    List<Object[]> thongKeThietBiDaDuocMuon(LocalDateTime startTime, LocalDateTime endTime, Long maTB);


    @Query("SELECT FUNCTION('DATE_FORMAT', t.tgMuon, '%d-%m-%Y %H:%i:%s') " +
            "FROM ThongTinSD t WHERE t.tgMuon BETWEEN ?1 AND ?2 " +
            "AND t.tgTra IS NULL AND (?3 IS NULL OR ?3 = 'tatca' OR t.maTB.maTB = CAST(?3 AS Long))" +
            "GROUP BY FUNCTION('DATE_FORMAT', t.tgMuon, '%d-%m-%Y %H:%i:%s')" +
            "ORDER BY FUNCTION('DATE_FORMAT', t.tgMuon, '%d-%m-%Y %H:%i:%s')")
    List<Object[]> thongKeThietBiDangDuocMuon(LocalDateTime startTime, LocalDateTime endTime, Long maTB);

    @Query("SELECT FUNCTION('DATE_FORMAT', x.ngayXL, '%d-%m-%Y') " +
            "FROM XuLy x WHERE x.ngayXL BETWEEN ?1 AND ?2 " +
            "AND x.trangThaiXL = ?3" +
            " GROUP BY FUNCTION('DATE_FORMAT', x.ngayXL, '%d-%m-%Y %H:%i:%s')" +
            "ORDER BY FUNCTION('DATE_FORMAT', x.ngayXL, '%d-%m-%Y %H:%i:%s')")
    List<Object[]> thongKeXuLy(LocalDateTime startTime, LocalDateTime endTime, boolean trangThai);

    // Các phương thức thống kê trả về dưới dạng bảng
    @Query("SELECT t.maTV.maTV, t.maTV.hoTen, t.maTV.khoa, t.maTV.nganh, " +
            "FUNCTION('DATE_FORMAT', t.tgVao, '%d-%m-%Y %H:%i:%s') " +
            "FROM ThongTinSD t WHERE t.tgVao BETWEEN ?1 AND ?2 " +
            "AND (?3 IS NULL OR ?3 = 'tatca' OR t.maTV.khoa = ?3) " +
            "AND (?4 IS NULL OR ?4 = 'tatca' OR t.maTV.nganh = ?4)")
    List<Object[]> thongKeThanhVienVaoKhuHocTapTable(LocalDateTime startTime, LocalDateTime endTime, String khoa, String nganh);

    @Query("SELECT t.maTB.maTB, t.maTB.tenTB, t.maTV.maTV, t.maTV.hoTen, " +
            "FUNCTION('DATE_FORMAT', t.tgMuon, '%d-%m-%Y %H:%i:%s'), " +
            "FUNCTION('DATE_FORMAT', t.tgTra, '%d-%m-%Y %H:%i:%s') " +
            "FROM ThongTinSD t WHERE t.tgMuon BETWEEN ?1 AND ?2 " +
            "AND t.tgTra IS NOT NULL AND (?3 IS NULL OR ?3 = 'tatca' OR t.maTB.maTB = CAST(?3 AS Long))")
    List<Object[]> thongKeThietBiDaDuocMuonTable(LocalDateTime startTime, LocalDateTime endTime, Long maTB);

    @Query("SELECT t.maTB.maTB, t.maTB.tenTB, t.maTV.maTV, t.maTV.hoTen, " +
            "FUNCTION('DATE_FORMAT', t.tgMuon, '%d-%m-%Y %H:%i:%s') " +
            "FROM ThongTinSD t WHERE t.tgMuon BETWEEN ?1 AND ?2 " +
            "AND t.tgTra IS NULL AND (?3 IS NULL OR ?3 = 'tatca' OR t.maTB.maTB = CAST(?3 AS Long))")
    List<Object[]> thongKeThietBiDangDuocMuonTable(LocalDateTime startTime, LocalDateTime endTime, Long maTB);

    @Query("SELECT x.maXL, x.maTV.hoTen, x.hinhThucXL, x.soTien, " +
            "FUNCTION('DATE_FORMAT', x.ngayXL, '%d-%m-%Y') " +
            "FROM XuLy x WHERE x.ngayXL BETWEEN ?1 AND ?2 " +
            "AND x.trangThaiXL = ?3")
    List<Object[]> thongKeXuLyTable(LocalDateTime startTime, LocalDateTime endTime, boolean trangThai);
}

