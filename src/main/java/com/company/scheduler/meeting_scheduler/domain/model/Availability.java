package com.company.scheduler.meeting_scheduler.domain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Intervalo de disponibilidad de un recurso (usuario o sala).
 *
 * Contiene:
 *  - ownerType (USER o ROOM)
 *  - ownerId
 *  - start y end del intervalo
 *
 * Se usa para:
 *  - Determinar si un usuario o sala puede participar en una reunión
 *  - Evitar conflictos de horario
 */
@Entity
public class Availability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ownerType; // USER o ROOM
    private String ownerId;

    @Column(name = "start_time") // mapeo correcto
    private LocalDateTime start;

    @Column(name = "end_time") // mapeo correcto
    private LocalDateTime end;

    protected Availability() {
        // requerido por JPA
    }

    public Availability(String ownerType, String ownerId, LocalDateTime start, LocalDateTime end) {
        this.ownerType = ownerType;
        this.ownerId = ownerId;
        this.start = start;
        this.end = end;
    }

    // ----- Getters -----
    public Long getId() {
        return id;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    // ----- Setters -----
    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
}