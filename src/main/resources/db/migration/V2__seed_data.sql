-- ===============================
-- LIMPIEZA INICIAL
-- ===============================
DELETE FROM meeting_participants;
DELETE FROM scheduled_meeting;
DELETE FROM meeting_request;
DELETE FROM availabilities;
DELETE FROM room_equipment;
DELETE FROM room;
DELETE FROM users;

-- ===============================
-- USUARIOS
-- ===============================
INSERT INTO users (id, name, timezone) VALUES ('U1','Ana','America/Mexico_City');
INSERT INTO users (id, name, timezone) VALUES ('U2','Luis','America/Mexico_City');
INSERT INTO users (id, name, timezone) VALUES ('U3','Paty','America/Mexico_City');

-- ===============================
-- SALAS
-- ===============================
INSERT INTO room (id, name, capacity, location) VALUES ('R1','Valle',6,'B1-F2');
INSERT INTO room (id, name, capacity, location) VALUES ('R2','Cañón',12,'B1-F3');

-- ===============================
-- EQUIPAMIENTO DE SALAS
-- ===============================
INSERT INTO room_equipment (room_id, equipment) VALUES ('R1','TV');
INSERT INTO room_equipment (room_id, equipment) VALUES ('R2','TV');
INSERT INTO room_equipment (room_id, equipment) VALUES ('R2','Whiteboard');

-- ===============================
-- DISPONIBILIDADES DE USUARIOS
-- ===============================
INSERT INTO availabilities (owner_type, owner_id, start_time, end_time) VALUES ('USER','U1','2026-01-29 09:00:00','2026-01-29 12:00:00');
INSERT INTO availabilities (owner_type, owner_id, start_time, end_time) VALUES ('USER','U2','2026-01-29 09:30:00','2026-01-29 11:30:00');
INSERT INTO availabilities (owner_type, owner_id, start_time, end_time) VALUES ('USER','U3','2026-01-29 10:00:00','2026-01-29 12:00:00');

-- ===============================
-- DISPONIBILIDADES DE SALAS
-- ===============================
INSERT INTO availabilities (owner_type, owner_id, start_time, end_time) VALUES ('ROOM','R1','2026-01-29 09:00:00','2026-01-29 13:00:00');
INSERT INTO availabilities (owner_type, owner_id, start_time, end_time) VALUES ('ROOM','R2','2026-01-29 09:00:00','2026-01-29 13:00:00');

--SALA NO DISPONIBLE
INSERT INTO room (id, name, capacity, location)
VALUES ('ROOM-FAILED', 'Sin Sala Disponible', 0, 'N/A');

-- ===============================
-- SOLICITUDES DE REUNIONES
-- ===============================
INSERT INTO meeting_request (id, organizer_id, duration_minutes, priority) VALUES ('M1','U1',60,'HIGH');
INSERT INTO meeting_request (id, organizer_id, duration_minutes, priority) VALUES ('M2','U2',30,'MEDIUM');

-- ===============================
-- PARTICIPANTES
-- ===============================
INSERT INTO meeting_participants (meeting_id, user_id) VALUES ('M1','U1');
INSERT INTO meeting_participants (meeting_id, user_id) VALUES ('M1','U2');
INSERT INTO meeting_participants (meeting_id, user_id) VALUES ('M1','U3');
INSERT INTO meeting_participants (meeting_id, user_id) VALUES ('M2','U1');

-- ===============================
-- REUNIONES PROGRAMADAS
-- ===============================
INSERT INTO scheduled_meeting (meeting_request_id, room_id, start_time, end_time, status, score)
VALUES ('M1','R1','2026-01-29 09:30:00','2026-01-29 10:30:00','SCHEDULED',0);

INSERT INTO scheduled_meeting (meeting_request_id, room_id, start_time, end_time, status, score)
VALUES ('M2','R2','2026-01-29 10:00:00','2026-01-29 10:30:00','SCHEDULED',0);
--prueba MEETING_REQUEST_TIME_WINDOWS
INSERT INTO MEETING_REQUEST_TIME_WINDOWS (meeting_request_id, start_time, end_time)
VALUES ('M1','2026-02-06 10:00:00','2026-02-06 11:00:00');
