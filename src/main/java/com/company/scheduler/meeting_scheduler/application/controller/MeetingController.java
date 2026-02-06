package com.company.scheduler.meeting_scheduler.application.controller;

import com.company.scheduler.meeting_scheduler.application.service.GreedySchedulerService;
import com.company.scheduler.meeting_scheduler.domain.model.MeetingRequest;
import com.company.scheduler.meeting_scheduler.domain.model.ScheduledMeeting;
import com.company.scheduler.meeting_scheduler.domain.model.MeetingStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api")
public class MeetingController {

    private static final Logger log = LoggerFactory.getLogger(MeetingController.class);

    private final GreedySchedulerService schedulerService;

    public MeetingController(GreedySchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    // 1️ POST /meeting-requests
    @PostMapping("/meeting-requests")
    public ResponseEntity<MeetingRequest> createMeetingRequest(
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey,
            @RequestBody MeetingRequest request) {

        log.info("Creando MeetingRequest {}", request.getId());
        return ResponseEntity.ok(
                schedulerService.createMeetingRequest(request, idempotencyKey)
        );
    }

    // 2️ POST /schedule/run
    @PostMapping("/schedule/run")
    public ResponseEntity<List<ScheduledMeeting>> runScheduler() {

        log.info("Ejecutando scheduler");

        List<ScheduledMeeting> result = schedulerService.runScheduler();

        log.info("Scheduler finalizado. Total reuniones procesadas: {}", result.size());

        return ResponseEntity.ok(result);
    }

    // 3️ GET /schedule
    @GetMapping("/schedule")
    public ResponseEntity<List<ScheduledMeeting>> getSchedule() {

        log.info("Obteniendo schedule");

        List<ScheduledMeeting> meetings = schedulerService.getAllScheduledMeetings();

        log.info("Total reuniones encontradas: {}", meetings.size());

        return ResponseEntity.ok(meetings);
    }

    // 4 GET /meeting-requests/{id}/explain
    @GetMapping("/meeting-requests/{id}/explain")
    public ResponseEntity<?> explainMeeting(@PathVariable String id) {

        log.info("Solicitando explicación para MeetingRequest {}", id);

        return schedulerService.explainMeeting(id);
    }
    // 5️ POST /scheduled/{id}/cancel
    @PostMapping("/scheduled/{id}/cancel")
    public ResponseEntity<?> cancelScheduledMeeting(@PathVariable Long id) {

        log.info("Solicitud de cancelación para ScheduledMeeting {}", id);

        return schedulerService.cancelScheduledMeeting(id);
    }



}
