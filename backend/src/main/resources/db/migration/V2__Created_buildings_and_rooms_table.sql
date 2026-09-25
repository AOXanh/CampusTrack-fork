CREATE TABLE Buildings (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE Rooms (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    building_id BIGINT UNSIGNED NOT NULL,
    room_number INT NOT NULL,
    capacity INT NOT NULL,
    room_type VARCHAR(50) NOT NULL,
    criticality VARCHAR(50) NOT NULL,

    CONSTRAINT fk_rooms_building
        FOREIGN KEY (building_id) REFERENCES Buildings(id)
        ON DELETE CASCADE,
    CONSTRAINT uq_rooms_building_room_number
        UNIQUE (building_id, room_number)
);