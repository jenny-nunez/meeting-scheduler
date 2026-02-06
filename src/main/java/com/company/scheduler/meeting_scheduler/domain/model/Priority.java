/**
 * Nivel de importancia de una reunión.
 *
 * Valores posibles: HIGH, MEDIUM, LOW
 *
 * Se usa para:
 *  - Ordenar solicitudes en la estrategia de asignación Greedy
 *  - Desempatar entre opciones con el mismo score
 *
 * Forma parte del dominio y permite mantener reglas de negocio claras sobre prioridad.
 */


package com.company.scheduler.meeting_scheduler.domain.model;

public enum Priority {
    HIGH,
    MEDIUM,
    LOW
}