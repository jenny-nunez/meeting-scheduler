/**
 * Representa un intervalo de tiempo con una fecha/hora de inicio y una fecha/hora de fin.
 *
 * Esta clase se utiliza para definir ventanas de tiempo en las que pueden
 * programarse reuniones u otras actividades dentro del sistema de planificación.
 *
 * Ejemplo de uso:
 *     LocalDateTime inicio = LocalDateTime.of(2026, 2, 3, 9, 0);
 *     LocalDateTime fin = LocalDateTime.of(2026, 2, 3, 10, 0);
 *     TimeWindow ventana = new TimeWindow(inicio, fin);
 */

package com.company.scheduler.meeting_scheduler.domain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "meeting_request_time_windows")
public class TimeWindow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_request_id")
    private MeetingRequest meetingRequest;

    protected TimeWindow() {
        // requerido por JPA
    }

    public TimeWindow(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // ----- Getters y Setters -----
    public Long getId() {
        return id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public MeetingRequest getMeetingRequest() {
        return meetingRequest;
    }

    public void setMeetingRequest(MeetingRequest meetingRequest) {
        this.meetingRequest = meetingRequest;
    }
}