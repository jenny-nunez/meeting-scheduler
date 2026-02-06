package com.company.scheduler.meeting_scheduler.application.service;

import com.company.scheduler.meeting_scheduler.domain.model.*;
import com.company.scheduler.meeting_scheduler.infrastructure.repository.AvailabilityRepository;
import com.company.scheduler.meeting_scheduler.infrastructure.repository.RoomRepository;
import com.company.scheduler.meeting_scheduler.infrastructure.repository.ScheduledMeetingRepository;
import com.company.scheduler.meeting_scheduler.infrastructure.repository.MeetingRequestRepository;


import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * GreedySchedulerService
 *
 * Servicio de aplicación encargado de asignar reuniones a salas y horarios
 * usando un algoritmo Greedy (voraz). Su objetivo es:
 *  1. Cumplir todas las restricciones duras (hard constraints) de cada MeetingRequest.
 *  2. Minimizar penalizaciones por preferencias blandas (soft constraints).
 *  3. Generar explicaciones claras si una reunión no puede ser asignada.
 *
 * Este servicio no depende de la infraestructura directamente y puede ser
 * fácilmente testeado con mocks de repositorios.
 */
@Service
public class GreedySchedulerService {

    private static final Logger log = LoggerFactory.getLogger(GreedySchedulerService.class);

    private final RoomRepository roomRepository;
    private final AvailabilityRepository availabilityRepository;
    private final ScheduledMeetingRepository scheduledMeetingRepository;
    private final MeetingRequestRepository meetingRequestRepository;

    public GreedySchedulerService(RoomRepository roomRepository,
                                  AvailabilityRepository availabilityRepository,
                                  ScheduledMeetingRepository scheduledMeetingRepository,
                                  MeetingRequestRepository meetingRequestRepository) {
        this.roomRepository = roomRepository;
        this.availabilityRepository = availabilityRepository;
        this.scheduledMeetingRepository = scheduledMeetingRepository;
        this.meetingRequestRepository = meetingRequestRepository;
    }


    /**
     * Método de apoyo para verificar que las salas existen en BD.
     */
    public void debugRooms() {
        List<Room> rooms = roomRepository.findAll();
        log.info("Cantidad de salas encontradas: {}", rooms.size());

        for (Room room : rooms) {
            log.info("Sala -> id: {}, nombre: {}, ubicación: {}, capacidad: {}",
                    room.getId(),
                    room.getName(),
                    room.getLocation(),
                    room.getCapacity());
        }
    }

    /**
     * PASO 1 DEL GREEDY:
     * Intenta asignar una reunión a una sala y horario, validando hard constraints
     * y calculando penalizaciones por soft constraints.
     *
     * @param request MeetingRequest a agendar
     * @return ScheduledMeeting con estado SCHEDULED o FAILED y score de penalización
     */
    public ScheduledMeeting schedule(MeetingRequest request) {

        log.info("Iniciando scheduling para MeetingRequest {}", request.getId());

        // Traer todas las salas disponibles
        List<Room> rooms = roomRepository.findAll();

        // Obtener IDs de participantes y contarlos
        List<String> participants = request.getParticipants();
        int participantsCount = participants.size();

        log.info("Número de participantes: {}", participantsCount);

        // Listar participantes para debugging
        if (participantsCount > 0) {
            log.info("Participantes de la reunión {}:", request.getId());
            for (String userId : participants) {
                log.info(" - userId: {}", userId);
            }
        } else {
            log.warn("La reunión {} no tiene participantes asignados", request.getId());
        }

        // Variables para guardar conflictos y score de penalización
        List<HardConflict> hardConflicts = new ArrayList<>();  // <-- HardConflict
        List<String> softTradeoffs = new ArrayList<>();        // penalizaciones por preferencias
        int totalScore = 0;                                   // score acumulado

        // ==========================
        // PASO 1: Iterar salas y ventanas
        // ==========================
        for (Room room : rooms) {
            log.info("Evaluando sala {} (capacidad: {})", room.getName(), room.getCapacity());

            hardConflicts.clear();  // Reiniciar conflictos para cada sala

            // ----- HARD CONSTRAINTS -----
            if (room.getCapacity() < participantsCount) {
                hardConflicts.add(HardConflict.ROOM_CAPACITY_EXCEEDED);
            }

            if (!room.getEquipment().containsAll(request.getRequiredEquipment())) {
                hardConflicts.add(HardConflict.REQUIRED_EQUIPMENT_MISSING);
            }

            boolean allAvailable = participants.stream().allMatch(
                    userId -> availabilityRepository.isUserAvailable(userId, request.getTimeWindows())
            );
            boolean roomAvailable = availabilityRepository.isRoomAvailable(room.getId(), request.getTimeWindows());

            if (!allAvailable) hardConflicts.add(HardConflict.NO_COMMON_AVAILABILITY);
            if (!roomAvailable) hardConflicts.add(HardConflict.ROOM_OVERLAP);

            boolean fitsWindow = request.getTimeWindows().stream()
                    .anyMatch(window -> Duration.between(window.getStart(), window.getEnd()).toMinutes() >= request.getDurationMinutes());
            if (!fitsWindow) hardConflicts.add(HardConflict.OUTSIDE_TIME_WINDOW);

            // Saltar sala si hay conflictos duros
            if (!hardConflicts.isEmpty()) {
                log.info("Sala {} no válida por hard constraints: {}", room.getName(), hardConflicts);
                continue;
            }

            // ----- SOFT CONSTRAINTS -----
            if (Boolean.TRUE.equals(request.isPreferEarlier())) {
                LocalDateTime earliestStart = request.getTimeWindows().get(0).getStart();
                int penalty = (int) Duration.between(earliestStart, earliestStart.plusMinutes(request.getDurationMinutes())).toMinutes();
                totalScore += penalty;
                softTradeoffs.add("preferEarlier: +" + penalty);
            }

            Integer buffer = request.getBufferBetweenMeetingsMinutes();
            if (buffer != null && buffer > 0) {
                boolean bufferViolated = scheduledMeetingRepository.existsOverlap(room.getId(), participants, buffer);
                if (bufferViolated) {
                    int bufferPenalty = buffer;
                    totalScore += bufferPenalty;
                    softTradeoffs.add("bufferBetweenMeetingsMinutes violated: +" + bufferPenalty);
                }
            }

            // ---------------------------
            // ASIGNACIÓN DE REUNIÓN EXITOSA
            // ---------------------------
            ScheduledMeeting scheduled = new ScheduledMeeting(
                    request.getId(),
                    room.getId(), // <-- siempre asignamos roomId válido
                    request.getTimeWindows().get(0).getStart(),
                    request.getTimeWindows().get(0).getStart().plusMinutes(request.getDurationMinutes()),
                    MeetingStatus.SCHEDULED,
                    totalScore
            );

            scheduledMeetingRepository.save(scheduled);
            log.info("Reunión {} asignada a sala {} con score {}", request.getId(), room.getName(), totalScore);

            return scheduled;
        }

        // ==========================
        // SI NO HAY SALA VÁLIDA
        // ==========================
                LocalDateTime fallbackStart = LocalDateTime.of(1970, 1, 1, 0, 0);
                LocalDateTime fallbackEnd = fallbackStart.plusMinutes(request.getDurationMinutes() > 0 ? request.getDurationMinutes() : 1);

        // roomId en null para no violar FK
        ScheduledMeeting failedMeeting = new ScheduledMeeting(
                request.getId(),
                "ROOM-FAILED",
                fallbackStart,
                fallbackEnd,
                MeetingStatus.FAILED,
                totalScore
        );

        String explanationText = String.join(", ", hardConflicts.stream()
                .map(Enum::name)
                .toList());

        failedMeeting.setExplanation(explanationText);

        scheduledMeetingRepository.save(failedMeeting);

                log.warn("No se encontró ninguna sala válida para la reunión {}. Hard conflicts: {}", request.getId(), hardConflicts);

                ConflictExplanation explanation = new ConflictExplanation(
                        hardConflicts,   // ahora tipo List<HardConflict>
                        softTradeoffs,
                        participants
                );

                return failedMeeting;
            }
    //POST /meeting-requests – Crea solicitud
    @Transactional
    public MeetingRequest createMeetingRequest(MeetingRequest request, String idempotencyKey) {

        log.info("Recibido MeetingRequest {}, Idempotency-Key: {}", request.getId(), idempotencyKey);

        // Sincronizar duration con durationMinutes
        if (request.getDurationMinutes() != null && request.getDuration() == null) {
            request.setDuration(Duration.ofMinutes(request.getDurationMinutes()));
        }

        // Validar timeWindows
        if (request.getTimeWindows() == null || request.getTimeWindows().isEmpty()) {
            log.warn(
                    "MeetingRequest {} no tiene timeWindows definidos",
                    request.getId()
            );
        } else {
            log.info(
                    "MeetingRequest {} tiene {} timeWindows",
                    request.getId(),
                    request.getTimeWindows().size()
            );
        }

        // ===============================
        // VALIDACIONES PARA IDEMPONTENCY
        // ===============================

        if (request.getOrganizerId() == null || request.getOrganizerId().isBlank()) {
            throw new IllegalArgumentException("organizerId es obligatorio");
        }

        if (idempotencyKey != null && !idempotencyKey.isBlank()) {

            return meetingRequestRepository
                    .findByIdempotencyKey(idempotencyKey)
                    .map(existing -> {
                        log.info(
                                "MeetingRequest ya existe. NO se duplica. id={}, Idempotency-Key={}",
                                existing.getId(),
                                idempotencyKey
                        );
                        return meetingRequestRepository
                                .findById(existing.getId())
                                .orElse(existing);
                    })
                    .orElseGet(() -> {
                        log.info(
                                "MeetingRequest no existe. Se procede a guardar. id={}, Idempotency-Key={}",
                                request.getId(),
                                idempotencyKey
                        );
                        request.setIdempotencyKey(idempotencyKey);
                        return meetingRequestRepository.save(request);
                    });
        }

        log.info("Idempotency-Key no enviada. Se guarda MeetingRequest normalmente. id={}", request.getId());
        return meetingRequestRepository.save(request);
    }

    //POST /schedule/run – Ejecuta asignación
   public List<ScheduledMeeting> runScheduler() {

       log.info("Ejecutando scheduler para todas las MeetingRequest");

       List<MeetingRequest> requests = meetingRequestRepository.findAll();
       log.info("MeetingRequests encontradas: {}", requests.size());

       List<ScheduledMeeting> results = new ArrayList<>();

       for (MeetingRequest request : requests) {

           log.info("Procesando MeetingRequest {}", request.getId());

           ScheduledMeeting scheduled = schedule(request);
           results.add(scheduled);

           if (scheduled.getStatus() == MeetingStatus.SCHEDULED) {
               log.info(
                       "MeetingRequest {} SCHEDULED | roomId={} | score={}",
                       request.getId(),
                       scheduled.getRoomId(),
                       scheduled.getScore()
               );
           } else {
               log.warn(
                       "MeetingRequest {} FAILED | score={}",
                       request.getId(),
                       scheduled.getScore()
               );
           }
       }

       log.info(
               "Scheduler finalizado. Total procesadas: {} | SCHEDULED: {} | FAILED: {}",
               results.size(),
               results.stream().filter(r -> r.getStatus() == MeetingStatus.SCHEDULED).count(),
               results.stream().filter(r -> r.getStatus() == MeetingStatus.FAILED).count()
       );

       return results;
   }

    public List<ScheduledMeeting> getAllScheduledMeetings() {

        log.info("Consultando todas las reuniones agendadas");

        return scheduledMeetingRepository.findAll();
    }

    public ResponseEntity<?> explainMeeting(String meetingRequestId) {

        return scheduledMeetingRepository
                .findTopByMeetingRequestIdOrderByIdDesc(meetingRequestId)
                .map(meeting -> {

                    if (meeting.getStatus() == MeetingStatus.SCHEDULED) {
                        return ResponseEntity.ok(
                                "La reunión fue agendada exitosamente, no hay conflictos"
                        );
                    }

                    return ResponseEntity.ok(
                            meeting.getExplanation()
                    );
                })
                .orElse(ResponseEntity.notFound().build());
    }
    //POST /scheduled/{id}/cancel – Cancela y libera recursos
    public ResponseEntity<?> cancelScheduledMeeting(Long scheduledId) {

        return scheduledMeetingRepository.findById(scheduledId)
                .map(meeting -> {

                    if (meeting.getStatus() == MeetingStatus.CANCELLED) {
                        log.warn("ScheduledMeeting {} ya estaba cancelado", scheduledId);
                        return ResponseEntity.ok("La reunión ya estaba cancelada");
                    }

                    meeting.setStatus(MeetingStatus.CANCELLED);
                    scheduledMeetingRepository.save(meeting);

                    log.info(
                            "ScheduledMeeting {} cancelado correctamente. Recursos liberados.",
                            scheduledId
                    );

                    return ResponseEntity.ok("Reunión cancelada exitosamente");
                })
                .orElseGet(() -> {
                    log.warn("ScheduledMeeting {} no encontrado", scheduledId);
                    return ResponseEntity.notFound().build();
                });
    }


}