/**
 * Explicación de por qué una reunión no pudo ser agendada.
 *
 * Contiene:
 *  - hardConflicts: conflictos obligatorios que impidieron agendar
 *  - softTradeoffs: penalizaciones por preferencias no cumplidas
 *  - minimalBlockingSet: conjunto mínimo de elementos que causaron el conflicto
 *
 * Se usa para:
 *  - Proveer trazabilidad y transparencia al usuario o sistema
 *  - Facilitar replanificación y debugging
 */

package com.company.scheduler.meeting_scheduler.domain.model;

import java.util.List;

public class ConflictExplanation {

    // Reglas duras que impidieron la asignación
    private List<HardConflict> hardConflicts;

    // Preferencias que se sacrificaron
    private List<String> softTradeoffs;

    // Conjunto mínimo que bloquea (usuarios, sala, ventana)
    private List<String> minimalBlockingSet;

    public ConflictExplanation() {
    }

    public ConflictExplanation(List<HardConflict> hardConflicts,
                               List<String> softTradeoffs,
                               List<String> minimalBlockingSet) {
        this.hardConflicts = hardConflicts;
        this.softTradeoffs = softTradeoffs;
        this.minimalBlockingSet = minimalBlockingSet;
    }

    public List<HardConflict> getHardConflicts() {
        return hardConflicts;
    }

    public List<String> getSoftTradeoffs() {
        return softTradeoffs;
    }

    public List<String> getMinimalBlockingSet() {
        return minimalBlockingSet;
    }

    public void setHardConflicts(List<HardConflict> hardConflicts) {
        this.hardConflicts = hardConflicts;
    }

    public void setSoftTradeoffs(List<String> softTradeoffs) {
        this.softTradeoffs = softTradeoffs;
    }

    public void setMinimalBlockingSet(List<String> minimalBlockingSet) {
        this.minimalBlockingSet = minimalBlockingSet;
    }
}