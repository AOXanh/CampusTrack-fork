CREATE TABLE assets (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    room_id BIGINT UNSIGNED NOT NULL,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    brand VARCHAR(100) NULL,
    model VARCHAR(100) NULL,
    serial_number VARCHAR(100) NULL,
    status VARCHAR(50) NOT NULL,
    asset_condition VARCHAR(50) NOT NULL,
    criticality VARCHAR(50) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_assets_room
        FOREIGN KEY (room_id) REFERENCES rooms (id)
            ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE nfc_tags (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    asset_id BIGINT UNSIGNED NOT NULL,
    uid VARCHAR(100) NOT NULL,
    status VARCHAR(100) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_nfc_tags_uid (uid),
    CONSTRAINT fk_nfc_tags_asset
        FOREIGN KEY (asset_id) REFERENCES assets (id)
            ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE incidents (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    incident_number VARCHAR(50) NOT NULL,
    asset_id BIGINT UNSIGNED NULL,
    room_id BIGINT UNSIGNED NOT NULL,
    reported_by BIGINT UNSIGNED NOT NULL,
    assigned_to BIGINT UNSIGNED NULL,
    category VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    safety_hazard BOOLEAN NOT NULL DEFAULT FALSE,
    operational_impact BOOLEAN NOT NULL DEFAULT FALSE,
    affected_area INT NOT NULL DEFAULT 1,
    priority VARCHAR(50) NOT NULL,
    priority_score INT NOT NULL DEFAULT 0,
    status VARCHAR(100) NOT NULL,
    evaluated_at TIMESTAMP NULL DEFAULT NULL,
    resolved_at TIMESTAMP NULL DEFAULT NULL,
    closed_at TIMESTAMP NULL DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_incidents_number (incident_number),
    CONSTRAINT fk_incidents_asset
        FOREIGN KEY (asset_id) REFERENCES assets (id)
            ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_incidents_room
        FOREIGN KEY (room_id) REFERENCES rooms (id)
            ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_incidents_reported_by
        FOREIGN KEY (reported_by) REFERENCES users (id)
            ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_incidents_assigned_to
        FOREIGN KEY (assigned_to) REFERENCES users (id)
            ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE maintenance_records (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    incident_id BIGINT UNSIGNED NOT NULL,
    user_id BIGINT UNSIGNED NOT NULL,
    action VARCHAR(100) NOT NULL,
    remarks TEXT NULL,
    status VARCHAR(100) NOT NULL,
    completed_at TIMESTAMP NULL DEFAULT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_maintenance_incident
        FOREIGN KEY (incident_id) REFERENCES incidents (id)
            ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_maintenance_user
        FOREIGN KEY (user_id) REFERENCES users (id)
            ON DELETE RESTRICT ON UPDATE CASCADE
);

-- Should match the @UniqueConstraint on Room model ;D
ALTER TABLE rooms
    ADD CONSTRAINT uq_rooms_building_room UNIQUE (building_id, room_number);