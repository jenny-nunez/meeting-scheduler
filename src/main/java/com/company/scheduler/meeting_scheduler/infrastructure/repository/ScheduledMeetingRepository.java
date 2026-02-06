package com.company.scheduler.meeting_scheduler.infrastructure.repository;

import com.company.scheduler.meeting_scheduler.domain.model.ScheduledMeeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduledMeetingRepository extends JpaRepository<ScheduledMeeting, Long> {

    // Método para verificar si hay traslapes de reuniones en la misma sala o con los mismos participantes
    default boolean existsOverlap(String roomId, List<String> participantIds, int bufferMinutes) {
        // Recorremos todas las reuniones agendadas
        for (ScheduledMeeting meeting : findAll()) {
            // Checar buffer en la sala
            if (roomId.equals(meeting.getRoomId())) {
                // startBuffer = inicio - buffer, endBuffer = fin + buffer
                if (meeting.getStartTime() != null && meeting.getEndTime() != null) {
                    LocalDateTime startBuffer = meeting.getStartTime().minusMinutes(bufferMinutes);
                    LocalDateTime endBuffer = meeting.getEndTime().plusMinutes(bufferMinutes);
                    for (String pid : participantIds) {
                        if (pid.equals(meeting.getMeetingRequestId())) continue;
                        if (!meeting.getStartTime().isAfter(endBuffer) && !meeting.getEndTime().isBefore(startBuffer)) {
                            return true; // conflicto detectado
                        }
                    }
                }
            }
        }
        return false; // sin conflictos
    }
    Optional<ScheduledMeeting> findTopByMeetingRequestIdOrderByIdDesc(String meetingRequestId);


}