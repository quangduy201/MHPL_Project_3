package com.example.project_3.services.impl;

import com.example.project_3.models.ThongTinSD;
import com.example.project_3.models.XuLy;
import com.example.project_3.services.ThongKeService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ThongKeServiceImpl implements ThongKeService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Object[]> thongKeThanhVienVaoKhuHocTap(LocalDateTime startTime, LocalDateTime endTime, String khoa, String nganh, boolean isTable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<ThongTinSD> root = cq.from(ThongTinSD.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.between(root.get("tgVao"), startTime, endTime));

        if (khoa != null && !khoa.isEmpty() && !"tatca".equals(khoa)) {
            predicates.add(cb.equal(root.get("maTV").get("khoa"), khoa));
        }

        if (nganh != null && !nganh.isEmpty() && !"tatca".equals(nganh)) {
            predicates.add(cb.equal(root.get("maTV").get("nganh"), nganh));
        }

        cq.select(isTable
                        ? cb.array(
                        root.get("maTV").get("maTV"),
                        root.get("maTV").get("hoTen"),
                        root.get("maTV").get("khoa"),
                        root.get("maTV").get("nganh"),
                        cb.function("DATE_FORMAT", String.class, root.get("tgVao"), cb.literal("%d-%m-%Y %H:%i:%s"))
                )
                        : cb.array(
                        cb.function("DATE_FORMAT", String.class, root.get("tgVao"), cb.literal("%d-%m-%Y")),
                        cb.count(root)
                )
        );

        if (!isTable) {
            cq.groupBy(
                    cb.function("DATE_FORMAT", String.class, root.get("tgVao"), cb.literal("%d-%m-%Y")),
                    root.get("tgVao")
            );
        }
        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.asc(cb.function("DATE_FORMAT", String.class, root.get("tgVao"), cb.literal("%d-%m-%Y %H:%i:%s"))));

        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public List<Object[]> thongKeThietBiDaDuocMuon(LocalDateTime startTime, LocalDateTime endTime, String maTB, boolean isTable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<ThongTinSD> root = cq.from(ThongTinSD.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.between(root.get("tgMuon"), startTime, endTime));
        predicates.add(cb.and(cb.isNotNull(root.get("tgMuon")), cb.isNotNull(root.get("tgTra"))));

        if (!"tatca".equals(maTB) && maTB != null && !maTB.isBlank() && !maTB.isEmpty()) {
            predicates.add(cb.equal(root.get("maTB").get("maTB"), maTB));
        }

        cq.select(isTable
                        ? cb.array(
                        root.get("maTB").get("maTB"),
                        root.get("maTB").get("tenTB"),
                        root.get("maTV").get("maTV"),
                        root.get("maTV").get("hoTen"),
                        cb.function("DATE_FORMAT", String.class, root.get("tgMuon"), cb.literal("%d-%m-%Y %H:%i:%s")),
                        cb.function("DATE_FORMAT", String.class, root.get("tgTra"), cb.literal("%d-%m-%Y %H:%i:%s"))
                )
                        : cb.array(
                        cb.function("DATE_FORMAT", String.class, root.get("tgMuon"), cb.literal("%d-%m-%Y")),
                        cb.count(root)
                )
        );

        if (!isTable) {
            cq.groupBy(
                    cb.function("DATE_FORMAT", String.class, root.get("tgMuon"), cb.literal("%d-%m-%Y")),
                    root.get("tgMuon")
            );
        }
        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.asc(cb.function("DATE_FORMAT", String.class, root.get("tgMuon"), cb.literal("%d-%m-%Y %H:%i:%s"))));

        System.out.println(entityManager.createQuery(cq).getResultList());

        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public List<Object[]> thongKeThietBiDangDuocMuon(LocalDateTime startTime, LocalDateTime endTime, String maTB, boolean isTable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<ThongTinSD> root = cq.from(ThongTinSD.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.between(root.get("tgMuon"), startTime, endTime));
        predicates.add(cb.and(cb.isNotNull(root.get("tgMuon")), cb.isNull(root.get("tgTra"))));

        if (!"tatca".equals(maTB)) {
            predicates.add(cb.equal(root.get("maTB").get("maTB"), maTB));
        }

        cq.select(isTable
                        ? cb.array(
                        root.get("maTB").get("maTB"),
                        root.get("maTB").get("tenTB"),
                        root.get("maTV").get("maTV"),
                        root.get("maTV").get("hoTen"),
                        cb.function("DATE_FORMAT", String.class, root.get("tgMuon"), cb.literal("%d-%m-%Y %H:%i:%s"))
                )
                        : cb.array(
                        cb.function("DATE_FORMAT", String.class, root.get("tgMuon"), cb.literal("%d-%m-%Y")),
                        cb.count(root)
                )
        );

        if (!isTable) {
            cq.groupBy(
                    cb.function("DATE_FORMAT", String.class, root.get("tgMuon"), cb.literal("%d-%m-%Y")),
                    root.get("tgMuon")
            );
        }
        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.asc(cb.function("DATE_FORMAT", String.class, root.get("tgMuon"), cb.literal("%d-%m-%Y %H:%i:%s"))));

        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public List<Object[]> thongKeXuLy(LocalDateTime startTime, LocalDateTime endTime, boolean trangThai, boolean isTable) {

        // Criteria API approach
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<XuLy> root = cq.from(XuLy.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.between(root.get("ngayXL"), startTime, endTime));

        predicates.add(cb.equal(root.get("trangThaiXL"), trangThai ? 1 : 0));

        cq.select(isTable
                        ? cb.array(
                        root.get("maXL"),
                        root.get("maTV").get("hoTen"),
                        root.get("hinhThucXL"),
                        root.get("soTien"),
                        cb.function("DATE_FORMAT", String.class, root.get("ngayXL"), cb.literal("%d-%m-%Y"))
                )
                        : cb.array(
                        cb.function("DATE_FORMAT", String.class, root.get("ngayXL"), cb.literal("%d-%m-%Y")),
                        cb.count(root.get("maXL")),
                        cb.sum(root.get("soTien"))
                )
        );

        if (!isTable) {
            cq.groupBy(
                    cb.function("DATE_FORMAT", String.class, root.get("ngayXL"), cb.literal("%d-%m-%Y")),
                    root.get("ngayXL")
            );
        }
        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.asc(cb.function("DATE_FORMAT", String.class, root.get("ngayXL"), cb.literal("%d-%m-%Y"))));

        return entityManager.createQuery(cq).getResultList();
    }
}