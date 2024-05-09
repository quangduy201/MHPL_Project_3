package com.example.project_3.services.impl;


import com.example.project_3.models.ThietBi;
import com.example.project_3.repositories.ThietBiRepository;
import com.example.project_3.repositories.ThongTinSDRepository;
import com.example.project_3.services.ThietBiService;
import com.example.project_3.specifications.BaseSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ThietBiServiceImpl implements ThietBiService {
    @Autowired
    private ThietBiRepository thietBiRepository;

    @Autowired
    private ThongTinSDRepository thongTinSDRepository;

    @Override
    public Page<ThietBi> getAllThietBi(Map<String, String> requestParams) {
        Specification<ThietBi> specification;

        if (requestParams == null || requestParams.isEmpty()) {
            specification = Specification.where(null);
        } else if (requestParams.containsKey("all")) {
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

        LocalDateTime date;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        assert requestParams != null;
        if (requestParams.containsKey("date")) {
            System.out.println(requestParams.get("date"));
            date = LocalDateTime.parse(requestParams.get("date"), formatter);
        } else {
            date = LocalDateTime.now();
        }

        ZoneOffset zoneOffset = ZoneOffset.ofHours(0);
        Instant instant = date.toInstant(zoneOffset);

        List<ThietBi> thietBiByNoBookingOrNotBorrowed = new ArrayList<>();
        List<ThietBi> thietBiByDate = thongTinSDRepository.findThietBiByDate(instant);

        thietBiByNoBookingOrNotBorrowed.addAll(thietBiByDate);

        // Tạo một danh sách các ID của các thiết bị
        List<Long> thietBiIds = thietBiByNoBookingOrNotBorrowed.stream()
                .map(ThietBi::getMaTB)
                .collect(Collectors.toList());

        // Tạo một Specification mới để lọc các thiết bị có ID không nằm trong danh sách trên
        Specification<ThietBi> noBookingOrNotBorrowedSpec = (root, query, criteriaBuilder) -> criteriaBuilder.not(root.get("id").in(thietBiIds));

        // Kết hợp Specification mới với Specification cũ
        specification = specification.and(noBookingOrNotBorrowedSpec);

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
