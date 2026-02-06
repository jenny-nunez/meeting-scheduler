package com.company.scheduler.meeting_scheduler.application.controller;

import com.company.scheduler.meeting_scheduler.application.service.AvailabilityService;
import com.company.scheduler.meeting_scheduler.domain.model.Availability;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserAvailabilityController {

    private static final Logger log = LoggerFactory.getLogger(UserAvailabilityController.class);

    private final AvailabilityService availabilityService;

    public UserAvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    // GET /users/{id}/availability
    @GetMapping("/{id}/availability")
    public ResponseEntity<List<Availability>> getUserAvailability(@PathVariable String id) {

        log.info("Solicitud de disponibilidad para usuario {}", id);

        List<Availability> availability = availabilityService.getUserAvailability(id);

        return ResponseEntity.ok(availability);
    }
}