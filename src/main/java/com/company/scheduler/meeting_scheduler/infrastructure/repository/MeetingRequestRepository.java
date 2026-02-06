package com.company.scheduler.meeting_scheduler.infrastructure.repository;

import com.company.scheduler.meeting_scheduler.domain.model.MeetingRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository public interface MeetingRequestRepository extends JpaRepository<MeetingRequest, String> {
    /** * Obtiene todas las solicitudes ordenadas por prioridad y duración.
    List<MeetingRequest> findAllByOrderByPriorityDescDurationMinutesDesc();
    /** * Permite implementar idempotencia en POST /meeting-requests */
    Optional<MeetingRequest> findByIdempotencyKey(String idempotencyKey);
}
