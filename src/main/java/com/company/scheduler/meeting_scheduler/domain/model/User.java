package com.company.scheduler.meeting_scheduler.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalTime;

@Entity
public class User {

    @Id
    private String id;

    private String name;
    private String email;

    private LocalTime workStart;
    private LocalTime workEnd;

    // Por ahora simple (luego lo refinamos)
    private String timezone;

    protected User() {
        // requerido por JPA
    }

    public User(
            String id,
            String name,
            String email,
            LocalTime workStart,
            LocalTime workEnd,
            String timezone
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.workStart = workStart;
        this.workEnd = workEnd;
        this.timezone = timezone;
    }

    // getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public LocalTime getWorkStart() {
        return workStart;
    }

    public LocalTime getWorkEnd() {
        return workEnd;
    }

    public String getTimezone() {
        return timezone;
    }
}