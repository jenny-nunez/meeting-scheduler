/**
 * Solicitud de reunión realizada por un organizador.
 *
 * Contiene:
 *  - organizerId, lista de participantes
 *  - duración, ventanas de tiempo permitidas
 *  - restricciones duras (hard) y blandas (soft)
 *  - prioridad de la reunión
 *
 * Se usa para:
 *  - Generar posibles asignaciones de sala y horario
 *  - Evaluar score y penalizaciones según preferencias
 */

package com.company.scheduler.meeting_scheduler.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class MeetingRequest {

    @Id
    private String id;

    private String organizerId;

    @Column(unique = true)
    private String idempotencyKey;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "meeting_participants",
            joinColumns = @JoinColumn(name = "meeting_id")
    )
    @Column(name = "user_id")
    private List<String> participants;

    @Transient
    private Duration duration;

    private Long durationMinutes;

    // 🔹 Mapeo explícito de la tabla de ventanas de tiempo
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "meeting_request_time_windows",
            joinColumns = @JoinColumn(name = "meeting_request_id")
    )
    private List<TimeWindow> timeWindows;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "meeting_request_required_equipment",
            joinColumns = @JoinColumn(name = "meeting_request_id")
    )
    @Column(name = "required_equipment")
    private List<String> requiredEquipment;

    private Boolean preferEarlier;
    private Integer bufferBetweenMeetingsMinutes;

    protected MeetingRequest() {
        // requerido por JPA
    }

    // ======================
    // GETTERS
    // ======================
    public String getId() { return id; }
    public String getOrganizerId() { return organizerId; }
    public List<String> getParticipants() { return participants; }
    public Duration getDuration() { return duration; }
    public Long getDurationMinutes() { return durationMinutes; }
    public List<TimeWindow> getTimeWindows() { return timeWindows; }
    public Priority getPriority() { return priority; }
    public List<String> getRequiredEquipment() { return requiredEquipment; }
    public Boolean isPreferEarlier() { return preferEarlier; }
    public Integer getBufferBetweenMeetingsMinutes() { return bufferBetweenMeetingsMinutes; }
    public String getIdempotencyKey() { return idempotencyKey; }

    // ======================
    // SETTERS
    // ======================
    public void setId(String id) { this.id = id; }
    public void setOrganizerId(String organizerId) { this.organizerId = organizerId; }
    public void setParticipants(List<String> participants) { this.participants = participants; }
    public void setDuration(Duration duration) {
        this.duration = duration;
        this.durationMinutes = (duration != null) ? duration.toMinutes() : null;
    }
    public void setTimeWindows(List<TimeWindow> timeWindows) { this.timeWindows = timeWindows; }
    public void setPriority(Priority priority) { this.priority = priority; }
    public void setRequiredEquipment(List<String> requiredEquipment) { this.requiredEquipment = requiredEquipment; }
    public void setPreferEarlier(Boolean preferEarlier) { this.preferEarlier = preferEarlier; }
    public void setBufferBetweenMeetingsMinutes(Integer bufferBetweenMeetingsMinutes) { this.bufferBetweenMeetingsMinutes = bufferBetweenMeetingsMinutes; }
    public void setIdempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; }

    // ======================
    // VALUE OBJECT
    // ======================
    @Embeddable
    public static class TimeWindow {

        @JsonProperty("start")
        @Column(name = "start_time")
        private LocalDateTime start;

        @JsonProperty("end")
        @Column(name = "end_time")
        private LocalDateTime end;

        public TimeWindow() {} // necesario para Jackson

        public TimeWindow(LocalDateTime start, LocalDateTime end) {
            this.start = start;
            this.end = end;
        }

        public LocalDateTime getStart() { return start; }
        public LocalDateTime getEnd() { return end; }
        public void setStart(LocalDateTime start) { this.start = start; }
        public void setEnd(LocalDateTime end) { this.end = end; }
    }
}