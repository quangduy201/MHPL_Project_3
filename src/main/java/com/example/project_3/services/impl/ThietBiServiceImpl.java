package com.example.project_3.services.impl;

import com.example.project_3.models.ThanhVien;
import com.example.project_3.models.ThietBi;
import com.example.project_3.models.ThongTinSD;
import com.example.project_3.repositories.ThanhVienRepository;
import com.example.project_3.repositories.ThietBiRepository;
import com.example.project_3.services.ThanhVienService;
import com.example.project_3.services.ThietBiService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.data.domain.Page;

@Service
public class ThietBiServiceImpl implements ThietBiService {

    @Autowired
    private ThietBiRepository thietBiRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ThietBi> getAllThietBi() {
        return thietBiRepository.findAll();
    }

    @Override
    public ThietBi getThietBiById(Long maTB) {
        return thietBiRepository.findById(maTB).orElse(null);
    }

    @Override
    public ThietBi saveThietBi(ThietBi tv) {
        return thietBiRepository.save(tv);
    }

    @Override
    public void deleteThietBiById(Long tv) {
        thietBiRepository.deleteById(tv);
    }
    public Page<ThietBi> getThietBiDangMuonByMaTV(Long maTV) {
        return thietBiRepository.findThietBiByMaTVEqualsAndTgMuonIsNotNullAndTgTraIsNull(maTV, null);
    }
    @Override
    public List<ThietBi> getAllThietBiDangMuonById(Long maTV) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ThietBi> cq = cb.createQuery(ThietBi.class);
        Root<ThongTinSD> root = cq.from(ThongTinSD.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.and(cb.isNotNull(root.get("tgMuon")), cb.isNull(root.get("tgTra"))));
        predicates.add(cb.equal(root.get("maTV").get("maTV"), maTV));

        cq.multiselect(root.get("maTB"), root.get("tenTB"), root.get("tgMuon"))
                .distinct(true)
                .where(predicates.toArray(new Predicate[0]))
                .orderBy(cb.asc(root.get("tgMuon")));

        List<ThietBi> borrowedDevicesList = entityManager.createQuery(cq).getResultList();
        return borrowedDevicesList;
        
    }
}
