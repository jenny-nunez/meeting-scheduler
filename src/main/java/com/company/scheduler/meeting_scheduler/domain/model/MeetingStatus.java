/**
 * Representa el estado de una reunión dentro del sistema de planificación.
 *
 * Los posibles estados son:
 * <ul>
 *   <li>{@link #SCHEDULED} - La reunión ha sido programada exitosamente.</li>
 *   <li>{@link #FAILED} - La reunión no pudo ser programada debido a algún error.</li>
 *   <li>{@link #CANCELLED} - La reunión fue cancelada después de haber sido programada.</li>
 * </ul>
 *
 * Este enum permite controlar el ciclo de vida de una reunión y facilita
 * la lógica de negocio relacionada con su gestión.
 */
package com.company.scheduler.meeting_scheduler.domain.model;

public enum MeetingStatus {
    SCHEDULED,
    FAILED,
    CANCELLED
}