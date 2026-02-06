-- ===============================
-- USERS
-- ===============================
CREATE TABLE users (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    timezone VARCHAR(50) NOT NULL
);

-- ===============================
-- ROOM
-- ===============================
CREATE TABLE room (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    capacity INT,
    location VARCHAR(50)
);

-- EQUIPAMIENTO DE SALAS
CREATE TABLE room_equipment (
    room_id VARCHAR(50) NOT NULL,
    equipment VARCHAR(100) NOT NULL,
    PRIMARY KEY (room_id, equipment),
    FOREIGN KEY (room_id) REFERENCES room(id)
);

-- ===============================
-- AVAILABILITIES
-- ===============================
CREATE TABLE availabilities (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    owner_type VARCHAR(50) NOT NULL, -- USER o ROOM
    owner_id VARCHAR(50) NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL
);

-- ===============================
-- RESERVATIONS
-- ===============================
CREATE TABLE reservations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    room_id VARCHAR(50) NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (room_id) REFERENCES room(id)
);

-- ===============================
-- MEETING_REQUEST
-- ===============================
CREATE TABLE meeting_request (
    id VARCHAR(50) PRIMARY KEY,
    organizer_id VARCHAR(50) NOT NULL,
    duration_minutes INT NOT NULL,
    priority VARCHAR(50) NOT NULL,
    prefer_earlier BOOLEAN,
    buffer_between_meetings_minutes INT,
    idempotency_key VARCHAR(100) UNIQUE,
    FOREIGN KEY (organizer_id) REFERENCES users(id)
);

-- PARTICIPANTES DE MEETING_REQUEST
CREATE TABLE meeting_participants (
    meeting_id VARCHAR(50) NOT NULL,
    user_id VARCHAR(50) NOT NULL,
    PRIMARY KEY (meeting_id, user_id),
    FOREIGN KEY (meeting_id) REFERENCES meeting_request(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- VENTANAS DE TIEMPO DE MEETING_REQUEST
CREATE TABLE meeting_request_time_windows (
    meeting_request_id VARCHAR(255) NOT NULL,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    CONSTRAINT fk_mr_timewindows FOREIGN KEY (meeting_request_id) REFERENCES meeting_request(id)
);

-- EQUIPAMIENTO REQUERIDO DE MEETING_REQUEST
CREATE TABLE MEETING_REQUEST_REQUIRED_EQUIPMENT (
    meeting_request_id VARCHAR(255) NOT NULL,
    required_equipment VARCHAR(255),
    CONSTRAINT fk_mr_required_equipment FOREIGN KEY (meeting_request_id) REFERENCES meeting_request(id)
);

CREATE TABLE meeting_request_participants (
    meeting_request_id VARCHAR(50) NOT NULL,
    participants VARCHAR(255),
    PRIMARY KEY(meeting_request_id),
    FOREIGN KEY(meeting_request_id) REFERENCES meeting_request(id)
);

-- SCHEDULED_MEETING

CREATE TABLE scheduled_meeting (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    meeting_request_id VARCHAR(50) NOT NULL,
    room_id VARCHAR(50) NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    status VARCHAR(20),
    score DOUBLE,
    explanation VARCHAR(1000),
    FOREIGN KEY (meeting_request_id) REFERENCES meeting_request(id),
    FOREIGN KEY (room_id) REFERENCES room(id)
);

CREATE TABLE availability (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    owner_id VARCHAR(50) NOT NULL,
    owner_type VARCHAR(20) NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL
);
