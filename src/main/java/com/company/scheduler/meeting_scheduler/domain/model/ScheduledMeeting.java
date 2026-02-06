package com.company.scheduler.meeting_scheduler.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

/**
 * Reunión asignada con sala y horario confirmados.
 *
 * Contiene:
 *  - meetingRequestId, roomId
 *  - startTime y endTime de la reunión
 *  - estado (SCHEDULED, FAILED, CANCELLED)
 *  - score calculado según preferencias
 *
 * Se usa para:
 *  - Registrar reuniones exitosas o fallidas
 *  - Documentar conflictos y decisiones de asignación
 */
@Entity
public class ScheduledMeeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String meetingRequestId;
    private String roomId;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private MeetingStatus status;

    private double score;

    @Column(length = 1000)
    private String explanation;

    protected ScheduledMeeting() {
        // requerido por JPA
    }

    public ScheduledMeeting(String meetingRequestId,
                            String roomId,
                            LocalDateTime startTime,
                            LocalDateTime endTime,
                            MeetingStatus status,
                            double score) {
        this.meetingRequestId = meetingRequestId;
        this.roomId = roomId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.score = score;
    }
    //getters
    public Long getId() {
        return id;
    }

    public String getMeetingRequestId() {
        return meetingRequestId;
    }

    public String getRoomId() {
        return roomId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public MeetingStatus getStatus() {
        return status;
    }

    public double getScore() {
        return score;
    }

    public String getExplanation() {
        return explanation;
    }
    // Setters
    public void setMeetingRequestId(String meetingRequestId) {
        this.meetingRequestId = meetingRequestId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setStatus(MeetingStatus status) {
        this.status = status;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}