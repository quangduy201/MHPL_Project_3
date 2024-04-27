package com.example.project_3.services.impl;

import com.example.project_3.models.ThanhVien;
import com.example.project_3.models.ThongTinSD;
import com.example.project_3.repositories.ThanhVienRepository;
import com.example.project_3.services.RegisterService;
import com.example.project_3.services.ThongKeService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
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

        if (khoa != null && !khoa.isEmpty()) {
            predicates.add(cb.equal(root.get("maTV").get("khoa"), khoa));
        }

        if (nganh != null && !nganh.isEmpty()) {
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
}
