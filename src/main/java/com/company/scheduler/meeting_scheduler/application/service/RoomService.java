package com.company.scheduler.meeting_scheduler.application.service;

import com.company.scheduler.meeting_scheduler.domain.model.Room;
import com.company.scheduler.meeting_scheduler.infrastructure.repository.RoomRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {

    private static final Logger log = LoggerFactory.getLogger(RoomService.class);

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> findRooms(String equipment, Integer capacity) {

        log.info("Buscando salas. Filtros -> equipment={}, capacity>={}", equipment, capacity);

        List<Room> rooms = roomRepository.findAll();

        // Filtrar por equipo si viene
        if (equipment != null && !equipment.isBlank()) {
            rooms = rooms.stream()
                    .filter(room -> room.getEquipment().contains(equipment))
                    .collect(Collectors.toList());

            log.info("Salas tras filtro equipment='{}': {}", equipment, rooms.size());
        }

        // Filtrar por capacidad si viene
        if (capacity != null && capacity > 0) {
            rooms = rooms.stream()
                    .filter(room -> room.getCapacity() >= capacity)
                    .collect(Collectors.toList());

            log.info("Salas tras filtro capacity>={}: {}", capacity, rooms.size());
        }

        return rooms;
    }
}