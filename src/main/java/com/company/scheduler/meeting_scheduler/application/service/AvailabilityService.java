package com.company.scheduler.meeting_scheduler.application.service;

import com.company.scheduler.meeting_scheduler.domain.model.Availability;
import com.company.scheduler.meeting_scheduler.infrastructure.repository.AvailabilityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvailabilityService {

    private static final Logger log = LoggerFactory.getLogger(AvailabilityService.class);

    private final AvailabilityRepository availabilityRepository;

    public AvailabilityService(AvailabilityRepository availabilityRepository) {
        this.availabilityRepository = availabilityRepository;
    }

    public List<Availability> getUserAvailability(String userId) {

        log.info("Consultando disponibilidad consolidada para usuario {}", userId);

        List<Availability> availabilities =
                availabilityRepository.findAll()
                        .stream()
                        .filter(a -> "USER".equals(a.getOwnerType()) && userId.equals(a.getOwnerId()))
                        .toList();

        log.info("Disponibilidades encontradas para usuario {}: {}", userId, availabilities.size());

        return availabilities;
    }
}
