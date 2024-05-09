package com.example.project_3.services.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import com.example.project_3.models.ThietBi;
import com.example.project_3.models.ThongTinSD;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.example.project_3.repositories.ThongTinSDRepository;

@Component
public class ScheduleServiceImpl {

    @Autowired
    private ThongTinSDRepository thongTinSDRepository;

    @Transactional
    @Scheduled(fixedRate = 60000)
    public void removeExpiredDatCho() {
        // Lấy thời gian hiện tại
        LocalDateTime now = LocalDateTime.now();
        ZoneOffset zoneOffset = ZoneOffset.ofHours(0);

        // Tính thời gian hết hạn (1 giờ trước thời điểm hiện tại)
        LocalDateTime expiredDateTime = now.minusHours(1);

        Instant expiredInstant = expiredDateTime.toInstant(zoneOffset);

        List<ThongTinSD> list = thongTinSDRepository.findTgDatchoNotNull();

        list.stream()
            .filter(l -> expiredInstant.isBefore(l.getTgDatcho()))
            .forEach(l -> {
                System.out.println("Removing record with tgDatcho: " + l.getTgDatcho().toString());
                thongTinSDRepository.delete(l);
            });
    }
}
