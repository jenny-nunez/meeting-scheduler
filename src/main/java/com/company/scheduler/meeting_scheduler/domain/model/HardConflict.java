/**
 * Usuario del sistema que puede organizar o participar en reuniones.
 *
 * Contiene:
 *  - id, nombre y correo del usuario
 *  - horarios de trabajo (workStart, workEnd)
 *  - zona horaria (timezone)
 *
 * Se usa para:
 *  - Validar disponibilidad y traslapes en reuniones
 *  - Ajustar horarios según la zona horaria
 *
 * Forma parte del dominio y no depende de frameworks ni persistencia.
 */
package com.company.scheduler.meeting_scheduler.domain.model;

public enum HardConflict {

    ROOM_CAPACITY_EXCEEDED,
    PARTICIPANT_OVERLAP,
    ROOM_OVERLAP,
    REQUIRED_EQUIPMENT_MISSING,
    OUTSIDE_TIME_WINDOW,
    NO_COMMON_AVAILABILITY
}