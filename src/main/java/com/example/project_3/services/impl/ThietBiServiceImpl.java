package com.example.project_3.services.impl;


import com.example.project_3.models.ThietBi;
import com.example.project_3.repositories.ThietBiRepository;
import com.example.project_3.repositories.ThongTinSDRepository;
import com.example.project_3.services.ThietBiService;
import com.example.project_3.specifications.BaseSpecification;
import jakarta.persistence.criteria.Predicate;
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

        LocalDateTime date;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        if (requestParams.containsKey("date")) {
            date = LocalDateTime.parse(requestParams.get("date"), formatter);
            requestParams.remove("date");
        } else {
            date = LocalDateTime.now();
        }

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
        }




        ZoneOffset zoneOffset = ZoneOffset.ofHours(0);
        LocalDateTime expiredDateTime = date.minusHours(1);
        Instant instant2 = date.toInstant(zoneOffset);
        Instant instant1 = expiredDateTime.toInstant(zoneOffset);

        List<ThietBi> thietBiByBookingOrBorrowed = new ArrayList<>();
        List<ThietBi> thietBiByDate = thongTinSDRepository.findThietBiByDate(instant1, instant2);

        thietBiByBookingOrBorrowed.addAll(thietBiByDate);

        // Tạo một danh sách các ID của các thiết bị
        List<Long> thietBiIds = thietBiByBookingOrBorrowed.stream()
                .map(ThietBi::getMaTB)
                .collect(Collectors.toList());


        // Tạo một Specification mới để lọc các thiết bị có ID không nằm trong danh sách trên
        Specification<ThietBi> noBookingOrNotBorrowedSpec = (root, query, criteriaBuilder) -> criteriaBuilder.not(root.get("id").in(thietBiIds));

        Specification<ThietBi> additionalSpec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Thêm các điều kiện lọc từ requestParams vào danh sách các predicates
            for (Map.Entry<String, String> entry : requestParams.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                if (key.equals("tenTB")) { // Thay someField bằng tên trường tương ứng trong ThietBi
                    predicates.add(criteriaBuilder.like(root.get(key), "%" + value + "%"));
                }
                // Thêm các trường và điều kiện lọc khác tương tự ở đây
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Specification<ThietBi> combinedSpec = noBookingOrNotBorrowedSpec.and(additionalSpec);

        String page = requestParams.get("page");
        Pageable pageable = Pageable.ofSize(4).withPage(0);
        if (page != null && page.trim().matches("^\\d+$"))
            pageable = pageable.withPage(Integer.parseInt(page) - 1);
        return thietBiRepository.findAll(combinedSpec, pageable);
    }

    @Override
    public List<ThietBi> getAllThietBi() {
        return thietBiRepository.findAll();
    }

    @Override
    public List<ThietBi> getAllThietBiByType(String type) {
        if (type.equals("muon")) {
            return thongTinSDRepository.findThietBiDangMuon();
        }

        if (type.equals("datcho")) {
            return thongTinSDRepository.findThietBiDangDatCho();
        }

        return getAllThietBi();
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
