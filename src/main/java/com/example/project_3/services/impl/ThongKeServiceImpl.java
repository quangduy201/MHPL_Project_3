package com.example.project_3.services.impl;

import com.example.project_3.models.ThongTinSD;
import com.example.project_3.models.XuLy;
import com.example.project_3.repositories.ThongTinSDRepository;
import com.example.project_3.repositories.XuLyRepository;
import com.example.project_3.services.ThongKeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ThongKeServiceImpl implements ThongKeService {
    @Autowired
    private ThongTinSDRepository thongTinSDRepository;

    @Autowired
    private XuLyRepository xuLyRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private List<XuLy> filterDateXLVP(List<XuLy> thongTinSDList, LocalDateTime startTime, LocalDateTime endTime) {
        OffsetDateTime offsetStartTime = startTime.atOffset(ZoneOffset.ofHours(7));
        OffsetDateTime offsetEndTime = endTime.atOffset(ZoneOffset.ofHours(7));

        Instant startInstant = offsetStartTime.toLocalDate().atTime(0, 0, 0)
                .atOffset(ZoneOffset.ofHours(7)).toInstant();
        Instant endInstant = offsetEndTime.toLocalDate().atTime(23, 59, 59)
                .atOffset(ZoneOffset.ofHours(7)).toInstant();

        return thongTinSDList.stream()
                .filter(tt -> tt.getNgayXL().compareTo(startInstant) >= 0 && tt.getNgayXL().compareTo(endInstant) <= 0)
                .sorted(Comparator.comparing(tt -> tt.getNgayXL().atZone(ZoneId.of("UTC+7")).toLocalDate()))
                .toList();
    }


    public List<Object[]> thongKeThanhVienVaoKhuHocTap(LocalDateTime startTime, LocalDateTime endTime, String khoa, String nganh, boolean isTable) {
        List<ThongTinSD> thongTinSDList = thongTinSDRepository.findAllThongTinSDOnlyTGVao();

        OffsetDateTime offsetStartTime = startTime.atOffset(ZoneOffset.ofHours(7));
        OffsetDateTime offsetEndTime = endTime.atOffset(ZoneOffset.ofHours(7));

        Instant startInstant = offsetStartTime.toLocalDate().atTime(0, 0, 0)
                .atOffset(ZoneOffset.ofHours(7)).toInstant();
        Instant endInstant = offsetEndTime.toLocalDate().atTime(23, 59, 59)
                .atOffset(ZoneOffset.ofHours(7)).toInstant();

        List<ThongTinSD> newThongTinSDList = thongTinSDList.stream()
                .filter(tt -> (tt.getTgVao().compareTo(startInstant) >= 0 && tt.getTgVao().compareTo(endInstant) <= 0) && ((Objects.equals(tt.getMaTV().getKhoa(), khoa) || Objects.equals(khoa, "tatca")) && (Objects.equals(tt.getMaTV().getNganh(), nganh) || Objects.equals(nganh, "tatca"))))
                .sorted(Comparator.comparing(tt -> tt.getTgVao().atZone(ZoneId.of("UTC+7")).toLocalDate()))
                .toList();

        List<Object[]> list = new ArrayList<>();

        if (isTable) {
            newThongTinSDList.forEach(tt -> list.add(
                new Object[] {
                    tt.getMaTV().getMaTV(),
                    tt.getMaTV().getHoTen(),
                    tt.getMaTV().getKhoa(),
                    tt.getMaTV().getNganh(),
                    tt.getTgVao().atZone(ZoneId.of("UTC+7")).toLocalDateTime().format(formatter)
                }
            ));
            return list;
        }

        Map<LocalDate, Long> countByDate = newThongTinSDList.stream()
                .map(thongTinSD -> thongTinSD.getTgVao().atZone(ZoneId.of("UTC+7")).toLocalDate())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        List<Map.Entry<LocalDate, Long>> sortedEntries = new ArrayList<>(countByDate.entrySet());

        // Sắp xếp danh sách theo ngày tăng dần
        sortedEntries.sort(Map.Entry.comparingByKey());

        // In ra số lần xuất hiện của mỗi ngày, đã sắp xếp theo ngày tăng dần
        for (Map.Entry<LocalDate, Long> entry : sortedEntries) {
            list.add(new Object[] {entry.getKey(), entry.getValue()});
        }

        return list;
    }

    @Override
    public List<Object[]> thongKeThietBiDaDuocMuon(LocalDateTime startTime, LocalDateTime endTime, String maTB, boolean isTable) {
        List<ThongTinSD> thongTinSDList = thongTinSDRepository.findAllThongTinSDOnlyThietBiDaMuon();

        thongTinSDList = thongTinSDList.stream().filter(tt -> Objects.equals(maTB, "tatca") || tt.getMaTB().getMaTB().toString().equals(maTB)).toList();

        OffsetDateTime offsetStartTime = startTime.atOffset(ZoneOffset.ofHours(7));
        OffsetDateTime offsetEndTime = endTime.atOffset(ZoneOffset.ofHours(7));

        Instant startInstant = offsetStartTime.toLocalDate().atTime(0, 0, 0)
                .atOffset(ZoneOffset.ofHours(7)).toInstant();
        Instant endInstant = offsetEndTime.toLocalDate().atTime(23, 59, 59)
                .atOffset(ZoneOffset.ofHours(7)).toInstant();


        List<ThongTinSD> newThongTinSDList = thongTinSDList.stream()
                .filter(tt -> tt.getTgMuon().compareTo(startInstant) >= 0 && tt.getTgMuon().compareTo(endInstant) <= 0)
                .sorted(Comparator.comparing(tt -> tt.getTgMuon().atZone(ZoneId.of("UTC+7")).toLocalDate()))
                .toList();

        List<Object[]> list = new ArrayList<>();

        if (isTable) {
            newThongTinSDList.forEach(tt -> list.add(
                    new Object[] {
                            tt.getMaTB().getMaTB(),
                            tt.getMaTB().getTenTB(),
                            tt.getMaTV().getMaTV(),
                            tt.getMaTV().getHoTen(),
                            tt.getTgMuon().atZone(ZoneId.of("UTC+7")).toLocalDateTime().format(formatter),
                            tt.getTgTra().atZone(ZoneId.of("UTC+7")).toLocalDateTime().format(formatter)
                    }
            ));
            return list;
        }

        Map<LocalDate, Long> countByDate = newThongTinSDList.stream()
                .map(thongTinSD -> thongTinSD.getTgMuon().atZone(ZoneId.of("UTC+7")).toLocalDate())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        List<Map.Entry<LocalDate, Long>> sortedEntries = new ArrayList<>(countByDate.entrySet());

        // Sắp xếp danh sách theo ngày tăng dần
        sortedEntries.sort(Map.Entry.comparingByKey());

        // In ra số lần xuất hiện của mỗi ngày, đã sắp xếp theo ngày tăng dần
        for (Map.Entry<LocalDate, Long> entry : sortedEntries) {
            list.add(new Object[] {entry.getKey(), entry.getValue()});
        }

        return list;
    }

    @Override
    public List<Object[]> thongKeThietBiDangDuocMuon(LocalDateTime startTime, LocalDateTime endTime, String maTB, boolean isTable) {
        List<ThongTinSD> thongTinSDList = thongTinSDRepository.findAllThongTinSDOnlyThietBiDangMuon();

        thongTinSDList = thongTinSDList.stream().filter(tt -> Objects.equals(maTB, "tatca") || tt.getMaTB().getMaTB().toString().equals(maTB)).toList();

        OffsetDateTime offsetStartTime = startTime.atOffset(ZoneOffset.ofHours(7));
        OffsetDateTime offsetEndTime = endTime.atOffset(ZoneOffset.ofHours(7));

        Instant startInstant = offsetStartTime.toLocalDate().atTime(0, 0, 0)
                .atOffset(ZoneOffset.ofHours(7)).toInstant();
        Instant endInstant = offsetEndTime.toLocalDate().atTime(23, 59, 59)
                .atOffset(ZoneOffset.ofHours(7)).toInstant();

        List<ThongTinSD> newThongTinSDList = thongTinSDList.stream()
                .filter(tt -> tt.getTgMuon().compareTo(startInstant) >= 0 && tt.getTgMuon().compareTo(endInstant) <= 0)
                .sorted(Comparator.comparing(tt -> tt.getTgMuon().atZone(ZoneId.of("UTC+7")).toLocalDate()))
                .toList();

        List<Object[]> list = new ArrayList<>();

        if (isTable) {
            newThongTinSDList.forEach(tt -> list.add(
                    new Object[] {
                            tt.getMaTB().getMaTB(),
                            tt.getMaTB().getTenTB(),
                            tt.getMaTV().getMaTV(),
                            tt.getMaTV().getHoTen(),
                            tt.getTgMuon().atZone(ZoneId.of("UTC+7")).toLocalDateTime().format(formatter)
                    }
            ));
            return list;
        }

        Map<LocalDate, Long> countByDate = newThongTinSDList.stream()
                .map(thongTinSD -> thongTinSD.getTgMuon().atZone(ZoneId.of("UTC+7")).toLocalDate())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        List<Map.Entry<LocalDate, Long>> sortedEntries = new ArrayList<>(countByDate.entrySet());

        // Sắp xếp danh sách theo ngày tăng dần
        sortedEntries.sort(Map.Entry.comparingByKey());

        // In ra số lần xuất hiện của mỗi ngày, đã sắp xếp theo ngày tăng dần
        for (Map.Entry<LocalDate, Long> entry : sortedEntries) {
            list.add(new Object[] {entry.getKey(), entry.getValue()});
        }

        return list;
    }

    @Override
    public List<Object[]> thongKeXuLy(LocalDateTime startTime, LocalDateTime endTime, boolean trangThai, boolean isTable) {

        List<XuLy> xuLyList = xuLyRepository.findAllXuLyViPham(trangThai ? 1 : 0);

        List<XuLy> newXuLyList = filterDateXLVP(xuLyList, startTime, endTime);

        List<Object[]> list = new ArrayList<>();

        if (isTable) {
            newXuLyList.forEach(tt -> list.add(
                    new Object[] {
                            tt.getMaXL(),
                            tt.getMaTV().getMaTV(),
                            tt.getMaTV().getHoTen(),
                            tt.getHinhThucXL(),
                            tt.getSoTien(),
                            tt.getNgayXL().atZone(ZoneId.of("UTC+7")).toLocalDateTime().format(formatter)
                    }
            ));
            return list;
        }

        Map<LocalDate, Long> countByDate = newXuLyList.stream()
                .map(thongTinSD -> thongTinSD.getNgayXL().atZone(ZoneId.of("UTC+7")).toLocalDate())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        List<Map.Entry<LocalDate, Long>> sortedEntries = new ArrayList<>(countByDate.entrySet());

        // Sắp xếp danh sách theo ngày tăng dần
        sortedEntries.sort(Map.Entry.comparingByKey());

        // In ra số lần xuất hiện của mỗi ngày, đã sắp xếp theo ngày tăng dần
        for (Map.Entry<LocalDate, Long> entry : sortedEntries) {
            list.add(new Object[] {entry.getKey(), entry.getValue()});
        }

        return list;
    }
}