package com.company.scheduler.meeting_scheduler.infrastructure.repository;

import com.company.scheduler.meeting_scheduler.domain.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {
    // Métodos personalizados opcionales
}
