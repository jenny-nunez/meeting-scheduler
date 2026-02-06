package com.company.scheduler.meeting_scheduler.application.controller;

import com.company.scheduler.meeting_scheduler.application.service.RoomService;
import com.company.scheduler.meeting_scheduler.domain.model.Room;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private static final Logger log = LoggerFactory.getLogger(RoomController.class);

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    // GET /api/rooms?equipment=TV&capacity=8
    @GetMapping
    public ResponseEntity<List<Room>> getRooms(
            @RequestParam(required = false) String equipment,
            @RequestParam(required = false) Integer capacity
    ) {

        log.info("Consultando salas con filtros -> equipment={}, capacity>={}",
                equipment, capacity);

        return ResponseEntity.ok(
                roomService.findRooms(equipment, capacity)
        );
    }
}