package com.company.scheduler.meeting_scheduler.infrastructure.repository;

import com.company.scheduler.meeting_scheduler.domain.model.Availability;
import com.company.scheduler.meeting_scheduler.domain.model.MeetingRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> { // <-- cambiar String por Long, ya que id es Long

    // -------------------------------
    // Usuarios
    // Devuelve disponibilidad completa del usuario en un rango específico
    // -------------------------------
    @Query("SELECT a FROM Availability a WHERE a.ownerType = 'USER' AND a.ownerId = :userId "
            + "AND a.start <= :start AND a.end >= :end") // <- aquí cambiamos a 'a.end'
    List<Availability> findUserAvailability(String userId, LocalDateTime start, LocalDateTime end);

    // -------------------------------
    // Salas
    // Devuelve disponibilidad completa de la sala en un rango específico
    // -------------------------------
    @Query("SELECT a FROM Availability a WHERE a.ownerType = 'ROOM' AND a.ownerId = :roomId "
            + "AND a.start <= :start AND a.end >= :end") // <- aquí también
    List<Availability> findRoomAvailability(String roomId, LocalDateTime start, LocalDateTime end);

    // -------------------------------
    // Métodos de conveniencia para usar en el Scheduler
    // -------------------------------
    default boolean isUserAvailable(String userId, List<MeetingRequest.TimeWindow> windows) {
        for (MeetingRequest.TimeWindow window : windows) {
            List<Availability> avail = findUserAvailability(userId, window.getStart(), window.getEnd());
            if (avail.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    default boolean isRoomAvailable(String roomId, List<MeetingRequest.TimeWindow> windows) {
        for (MeetingRequest.TimeWindow window : windows) {
            List<Availability> avail = findRoomAvailability(roomId, window.getStart(), window.getEnd());
            if (avail.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}