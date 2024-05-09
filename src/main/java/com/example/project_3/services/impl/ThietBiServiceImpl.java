package com.example.project_3.services.impl;


import com.example.project_3.models.ThietBi;
import com.example.project_3.repositories.ThietBiRepository;
import com.example.project_3.services.ThietBiService;
import com.example.project_3.specifications.BaseSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class ThietBiServiceImpl implements ThietBiService {
    private final ThietBiRepository thietBiRepository;

    @Autowired
    public ThietBiServiceImpl(ThietBiRepository thietBiRepository) {
        this.thietBiRepository = thietBiRepository;
    }

    @Override
    public Page<ThietBi> getAllThietBi(Map<String, String> requestParams) {
        Specification<ThietBi> specification;
        if (requestParams.containsKey("all")) {
            String value = requestParams.get("all");
            Map<String, String> params = Map.of(
                    "maTB", value,
                    "tenTB", value,
                    "motaTB", value
            );
            specification = BaseSpecification.buildLikeSpecification(params, false);
        } else {
            // maTB LIKE '%...%'
            // [AND tenTB LIKE '%...%' ]
            // [AND motaTB LIKE '%...%' ]
            specification = BaseSpecification.buildLikeSpecification(requestParams, true);
        }
        String page = requestParams.get("page");
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        if (page != null && page.trim().matches("^\\d+$"))
            pageable = pageable.withPage(Integer.parseInt(page) - 1);
        return thietBiRepository.findAll(specification, pageable);
    }

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

    @Override
    public Page<ThietBi> getThietBiDangMuonByMaTV(Long maTV) {
        return thietBiRepository.findThietBiByMaTVEqualsAndTgMuonIsNotNullAndTgTraIsNull(maTV, null);
    }

    @Override
    public Page<ThietBi> getThietBiDangDatChoByMaTV(Long maTV) {
        return thietBiRepository.findThietBiByMaTVEqualsAndTgDatchoIsNotNull(maTV, null);
    }
}
