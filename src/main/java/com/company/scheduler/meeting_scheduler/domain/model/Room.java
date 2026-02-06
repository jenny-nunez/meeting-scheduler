/**
 * Sala física disponible para reuniones.
 *
 * Contiene:
 *  - id, nombre y ubicación
 *  - capacidad máxima de participantes
 *  - equipamiento disponible (TV, Whiteboard, etc.)
 *
 * Se usa para:
 *  - Validar disponibilidad frente a solicitudes de reunión
 *  - Verificar capacidad y requerimientos de equipo
 *  - Evitar empalmes de reservas
 */

package com.company.scheduler.meeting_scheduler.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.FetchType;

import java.util.Set;

@Entity
public class Room {

    @Id
    private String id;

    private String name;
    private int capacity;
    private String location;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> equipment;

    protected Room() {
        // requerido por JPA
    }

    public Room(String id, String name, int capacity, String location, Set<String> equipment) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.location = location;
        this.equipment = equipment;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getLocation() {
        return location;
    }

    public Set<String> getEquipment() {
        return equipment;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setEquipment(Set<String> equipment) {
        this.equipment = equipment;
    }
}