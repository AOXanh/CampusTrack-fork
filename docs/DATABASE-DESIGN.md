# Database Design

## 1. Overview

The **Campus Asset & Incident Management System** uses a MySQL database to store information about campus users, buildings, rooms, assets, NFC tags, incidents, and maintenance activities.

The database is designed around the relationship between physical campus resources and their digital records. Assets are associated with rooms, and NFC tags can be attached to assets for identification. Incidents can either be associated with a specific asset or reported as a facility-related issue within a room.

The database also stores the information needed by the system's automated incident evaluation process, including safety hazards, operational impact, affected area, priority, and priority score.

---

## 2. Database Tables

The database consists of the following tables:

| Table | Purpose |
|---|---|
| `users` | Stores system users and their roles. |
| `buildings` | Stores campus building information. |
| `rooms` | Stores rooms and their building assignments. |
| `assets` | Stores physical campus assets and their current condition. |
| `nfc_tags` | Stores NFC tags registered to assets. |
| `incidents` | Stores reported maintenance and facility incidents. |
| `maintenance_records` | Stores maintenance actions performed for incidents. |

---

# 3. Users

The `users` table stores the accounts that can interact with the system.

Users may report incidents, perform maintenance activities, or manage system information depending on their assigned role.

### Columns

| Column | Data Type | Null | Key | Default | Description |
|---|---|---|---|---|---|
| `id` | `BIGINT` | No | PK, Unique, Auto Increment | None | Unique identifier of the user. |
| `created_at` | `TIMESTAMP` | No | | `CURRENT_TIMESTAMP()` | Date and time when the user was created. |
| `name` | `VARCHAR(100)` | No | | None | Full name of the user. |
| `email` | `VARCHAR(100)` | No | Unique | None | Email address of the user. |
| `password_hash` | `VARCHAR(255)` | No | | None | Hashed password of the user. |
| `user_role` | `VARCHAR(50)` | No | | None | Role assigned to the user. |

### `user_role`

The `user_role` column identifies what the user is allowed to do within the system.

The schema currently stores the role as a `VARCHAR(50)` rather than using a separate roles table.

The project defines the following roles:

- `GENERAL_USER`
- `TECHNICIAN`
- `ADMINISTRATOR`

---

# 4. Buildings

The `buildings` table stores the buildings registered in the campus.

A building can contain multiple rooms.

### Columns

| Column | Data Type | Null | Key | Default | Description |
|---|---|---|---|---|---|
| `id` | `BIGINT` | No | PK, Unique, Auto Increment | None | Unique identifier of the building. |
| `created_at` | `TIMESTAMP` | No | | `CURRENT_TIMESTAMP()` | Date and time when the building was created. |
| `name` | `VARCHAR(100)` | No | | None | Name of the building. |

### Relationships

- One building can have many rooms.
- Deleting a building also deletes its associated rooms.
- Updating a building's ID updates the corresponding `building_id` values.

```text
buildings
    │
    └── rooms
```

Relationship:

```text
rooms.building_id → buildings.id
```

Delete behavior:

```text
ON DELETE CASCADE
```

Update behavior:

```text
ON UPDATE CASCADE
```

---

# 5. Rooms

The `rooms` table represents physical rooms within campus buildings.

Rooms can contain multiple assets and can also be associated with incidents.

### Columns

| Column | Data Type | Null | Key | Default | Description |
|---|---|---|---|---|---|
| `id` | `BIGINT` | No | PK, Unique, Auto Increment | None | Unique identifier of the room. |
| `created_at` | `TIMESTAMP` | No | | `CURRENT_TIMESTAMP()` | Date and time when the room was created. |
| `room_number` | `VARCHAR(50)` | No | Unique with `building_id` | None | Room number or room identifier. |
| `building_id` | `BIGINT` | No | FK | None | Building where the room is located. |
| `capacity` | `INT` | No | | None | Maximum capacity of the room. |
| `room_type` | `VARCHAR(50)` | No | | None | Type or purpose of the room. |
| `criticality` | `VARCHAR(50)` | No | | None | Criticality level of the room. |

### `room_type`

Allowed room types:

| Value | Description |
|---|---|
| `CLASSROOM` | Standard classroom. |
| `LABORATORY` | Laboratory room. |
| `LECTURE_HALL` | Large lecture or teaching space. |
| `OFFICE` | Office space. |
| `SERVER_ROOM` | Room used for servers and related infrastructure. |
| `STORAGE` | Storage area. |
| `WORKSHOP` | Workshop or practical work area. |
| `CONFERENCE_ROOM` | Conference or meeting room. |
| `RESTROOM` | Restroom or washroom. |
| `OTHER` | Other room types not covered above. |

### `criticality`

Allowed criticality levels:

| Value | Description |
|---|---|
| `LOW` | Low importance or impact. |
| `NORMAL` | Normal importance or impact. |
| `HIGH` | High importance or impact. |
| `CRITICAL` | Critical importance or impact. |

### Constraints

The combination of `room_number` and `building_id` must be unique.

This allows the same room number to exist in different buildings while preventing duplicate room numbers within the same building.

Example:

```text
Building A → Room 101
Building B → Room 101
```

Both are valid because they belong to different buildings.

---

# 6. Assets

The `assets` table stores physical equipment and other resources located on campus.

Each asset belongs to a room and can optionally have an NFC tag registered to it.

### Columns

| Column | Data Type | Null | Key | Default | Description |
|---|---|---|---|---|---|
| `id` | `BIGINT` | No | PK, Unique, Auto Increment | None | Unique identifier of the asset. |
| `created_at` | `TIMESTAMP` | No | | `CURRENT_TIMESTAMP()` | Date and time when the asset was registered. |
| `room_id` | `BIGINT` | No | FK | None | Room where the asset is located. |
| `name` | `VARCHAR(100)` | No | | None | Name of the asset. |
| `category` | `VARCHAR(50)` | No | | None | Category of the asset. |
| `brand` | `VARCHAR(100)` | Yes | | `NULL` | Brand or manufacturer of the asset. |
| `model` | `VARCHAR(100)` | Yes | | `NULL` | Model of the asset. |
| `serial_number` | `VARCHAR(100)` | Yes | | `NULL` | Serial number of the asset. |
| `status` | `VARCHAR(50)` | No | | None | Current operational status of the asset. |
| `asset_condition` | `VARCHAR(50)` | No | | None | Current physical condition of the asset. |
| `criticality` | `VARCHAR(50)` | No | | None | Importance of the asset to campus operations. |

### `category`

Allowed asset categories:

- `COMPUTER`
- `PROJECTOR`
- `AIRCONDITIONER`
- `FURNITURE`
- `NETWORK_EQUIPMENT`
- `LAB_EQUIPMENT`
- `ELECTRICAL_EQUIPMENT`
- `OFFICE_EQUIPMENT`
- `OTHER`

### `status`

Allowed asset statuses:

- `IN_USE`
- `AVAILABLE`
- `UNDER_MAINTENANCE`
- `OUT_OF_SERVICE`
- `RETIRED`

### `asset_condition`

Allowed asset conditions:

- `GOOD`
- `FAIR`
- `DAMAGED`
- `POOR`

### `criticality`

Allowed criticality levels:

- `LOW`
- `NORMAL`
- `HIGH`
- `CRITICAL`

### Relationships

Each asset belongs to one room.

```text
rooms
  │
  └── assets
```

Relationship:

```text
assets.room_id → rooms.id
```

Delete behavior:

```text
ON DELETE CASCADE
```

Update behavior:

```text
ON UPDATE CASCADE
```

If a room is deleted, its associated assets are also deleted.

---

# 7. NFC Tags

The `nfc_tags` table stores NFC tags registered to physical assets.

An NFC tag provides a way for the system to identify an asset without requiring the user to manually search for it.

### Columns

| Column | Data Type | Null | Key | Default | Description |
|---|---|---|---|---|---|
| `id` | `BIGINT` | No | PK, Unique, Auto Increment | None | Unique identifier of the NFC tag record. |
| `created_at` | `TIMESTAMP` | No | | `CURRENT_TIMESTAMP()` | Date and time when the tag was registered. |
| `asset_id` | `BIGINT` | No | FK | None | Asset associated with the NFC tag. |
| `uid` | `VARCHAR(100)` | No | Unique | None | Unique identifier of the physical NFC tag. |
| `status` | `VARCHAR(100)` | No | | None | Current status of the NFC tag. |

### `status`

Allowed NFC tag statuses:

- `ACTIVE`
- `INACTIVE`
- `LOST`
- `DAMAGED`
- `REPLACED`

### Relationships

Each NFC tag belongs to an asset.

```text
assets
  │
  └── nfc_tags
```

Relationship:

```text
nfc_tags.asset_id → assets.id
```

Delete behavior:

```text
ON DELETE CASCADE
```

Update behavior:

```text
ON UPDATE CASCADE
```

Deleting an asset also deletes its associated NFC tags.

---

# 8. Incidents

The `incidents` table is the central table for maintenance and facility-related reports.

An incident may be connected to a specific asset, but this is not required. This allows the system to handle both asset-related problems and facility issues such as electrical, plumbing, structural, or other room-level problems.

The table also contains the structured information used by the automated incident evaluation process.

### Columns

| Column | Data Type | Null | Key | Default | Description |
|---|---|---|---|---|---|
| `id` | `BIGINT` | No | PK, Unique, Auto Increment | None | Unique identifier of the incident. |
| `created_at` | `TIMESTAMP` | No | | `CURRENT_TIMESTAMP()` | Date and time when the incident was reported. |
| `incident_number` | `VARCHAR(50)` | No | Unique | None | Human-readable unique incident identifier. |
| `asset_id` | `BIGINT` | Yes | FK | `NULL` | Asset associated with the incident, if applicable. |
| `assigned_to` | `BIGINT` | Yes | FK | `NULL` | User assigned to handle the incident. |
| `room_id` | `BIGINT` | No | FK | None | Room where the incident occurred. |
| `reported_by` | `BIGINT` | No | FK | None | User who reported the incident. |
| `category` | `VARCHAR(50)` | No | | None | Category of the incident. |
| `description` | `TEXT` | No | | None | Detailed description of the problem. |
| `safety_hazard` | `BOOLEAN` | No | | `FALSE` | Indicates whether the incident presents a safety hazard. |
| `operational_impact` | `BOOLEAN` | No | | `FALSE` | Indicates whether the incident affects normal operations. |
| `affected_area` | `INT` | No | | `1` | Number representing the affected area or scope. |
| `priority` | `VARCHAR(50)` | No | | None | Priority assigned after incident evaluation. |
| `priority_score` | `INT` | No | | `0` | Calculated score used by the priority evaluation system. |
| `status` | `VARCHAR(100)` | No | | None | Current state of the incident. |
| `evaluated_at` | `TIMESTAMP` | Yes | | `NULL` | Date and time when the incident was evaluated. |
| `resolved_at` | `TIMESTAMP` | Yes | | `NULL` | Date and time when the incident was resolved. |
| `closed_at` | `TIMESTAMP` | Yes | | `NULL` | Date and time when the incident was closed. |

### `category`

Allowed incident categories:

- `EQUIPMENT_FAILURE`
- `ELECTRICAL`
- `PLUMBING`
- `STRUCTURAL`
- `HVAC`
- `NETWORK_IT`
- `FURNITURE`
- `SAFETY_HAZARD`
- `OTHER`

### `priority`

Allowed priority levels:

| Value | Description |
|---|---|
| `LOW` | Incident with relatively low impact. |
| `MEDIUM` | Incident requiring normal attention. |
| `HIGH` | Incident with significant operational or safety impact. |
| `CRITICAL` | Incident requiring immediate attention. |

### `status`

The incident lifecycle is represented by the following statuses:

```text
REPORTED
    ↓
EVALUATED
    ↓
ASSIGNED
    ↓
IN_PROGRESS
    ↓
RESOLVED
    ↓
VERIFIED
    ↓
CLOSED
```

The timestamps provide additional information about important lifecycle events:

- `evaluated_at` records when evaluation was completed.
- `resolved_at` records when the incident was resolved.
- `closed_at` records when the incident was closed.

### Incident Evaluation Data

The following columns provide the information required by the automated evaluation system:

| Column | Purpose |
|---|---|
| `safety_hazard` | Identifies whether a reported issue presents a safety hazard. |
| `operational_impact` | Identifies whether normal campus operations are affected. |
| `affected_area` | Represents the scope of the affected area. |
| `priority_score` | Stores the calculated evaluation score. |
| `priority` | Stores the resulting priority classification. |

The reporter provides observable information about the incident. The evaluation process can then use these values together with other relevant information, such as asset or room criticality, to determine the incident's priority.

### Incident Relationships

An incident may reference an asset:

```text
assets
  │
  └── incidents
```

Because `asset_id` is nullable, an incident does not have to be associated with an asset.

An incident must belong to a room:

```text
rooms
  │
  └── incidents
```

An incident must also have a reporting user:

```text
users
  │
  └── incidents
```

An incident may have an assigned user:

```text
users
  │
  └── incidents
```

The assigned user is nullable because an incident may initially be unassigned.

### Foreign Key Behavior

| Foreign Key | Delete Behavior | Update Behavior |
|---|---|---|
| `incidents.asset_id` | `SET NULL` | `CASCADE` |
| `incidents.room_id` | `CASCADE` | `CASCADE` |
| `incidents.reported_by` | `RESTRICT` | `CASCADE` |
| `incidents.assigned_to` | `SET NULL` | `CASCADE` |

If an asset is deleted, the incident remains in the database and its `asset_id` becomes `NULL`.

If the assigned user is deleted, the incident remains in the database and its `assigned_to` becomes `NULL`.

The reporting user cannot be deleted while their reported incidents still reference their user record.

---

# 9. Maintenance Records

The `maintenance_records` table stores the actions performed while working on an incident.

An incident can have multiple maintenance records, allowing the system to keep a history of actions performed during maintenance.

### Columns

| Column | Data Type | Null | Key | Default | Description |
|---|---|---|---|---|---|
| `id` | `BIGINT` | No | PK, Unique, Auto Increment | None | Unique identifier of the maintenance record. |
| `created_at` | `TIMESTAMP` | No | | `CURRENT_TIMESTAMP()` | Date and time when the maintenance record was created. |
| `incident_id` | `BIGINT` | No | FK | None | Incident associated with the maintenance activity. |
| `user_id` | `BIGINT` | No | FK | None | User who performed or recorded the maintenance action. |
| `action` | `VARCHAR(100)` | No | | None | Description or name of the maintenance action. |
| `remarks` | `TEXT` | Yes | | `NULL` | Additional information about the action. |
| `status` | `VARCHAR(100)` | No | | None | Current status of the maintenance activity. |
| `completed_at` | `TIMESTAMP` | Yes | | `NULL` | Date and time when the maintenance activity was completed. |

### `status`

Allowed maintenance record statuses:

- `PENDING`
- `IN_PROGRESS`
- `COMPLETED`
- `CANCELLED`

### Relationships

Each maintenance record belongs to an incident.

```text
incidents
    │
    └── maintenance_records
```

Each maintenance record also references the user responsible for the maintenance action.

```text
users
    │
    └── maintenance_records
```

### Foreign Key Behavior

| Foreign Key | Delete Behavior | Update Behavior |
|---|---|---|
| `maintenance_records.incident_id` | `CASCADE` | `CASCADE` |
| `maintenance_records.user_id` | `RESTRICT` | `CASCADE` |

Deleting an incident also deletes its associated maintenance records.

A user cannot be deleted while their maintenance records still reference their user record.

---

# 10. Entity Relationships

The main relationships between the tables are:

```text
                    ┌──────────────┐
                    │   buildings  │
                    └──────┬───────┘
                           │
                           │ 1:N
                           ▼
                    ┌──────────────┐
                    │    rooms     │
                    └──────┬───────┘
                           │
                 ┌─────────┴─────────┐
                 │                   │
                1:N                 1:N
                 │                   │
                 ▼                   ▼
          ┌──────────────┐    ┌──────────────┐
          │    assets    │    │   incidents  │
          └──────┬───────┘    └──────┬───────┘
                 │                   │
                1:N                 1:N
                 │                   │
                 ▼                   ▼
          ┌──────────────┐    ┌──────────────────────┐
          │  nfc_tags    │    │ maintenance_records  │
          └──────────────┘    └──────────────────────┘
                                      │
                                      │ N:1
                                      ▼
                               ┌──────────────┐
                               │    users     │
                               └──────────────┘
```

Users also have relationships with incidents:

```text
users
  │
  ├── reported_by ──→ incidents
  │
  ├── assigned_to ──→ incidents
  │
  └──────────────────→ maintenance_records
```

---

# 11. Foreign Key Summary

| Child Table | Column | Parent Table | Parent Column | Delete | Update |
|---|---|---|---|---|---|
| `rooms` | `building_id` | `buildings` | `id` | CASCADE | CASCADE |
| `assets` | `room_id` | `rooms` | `id` | CASCADE | CASCADE |
| `nfc_tags` | `asset_id` | `assets` | `id` | CASCADE | CASCADE |
| `incidents` | `asset_id` | `assets` | `id` | SET NULL | CASCADE |
| `incidents` | `room_id` | `rooms` | `id` | CASCADE | CASCADE |
| `incidents` | `reported_by` | `users` | `id` | RESTRICT | CASCADE |
| `incidents` | `assigned_to` | `users` | `id` | SET NULL | CASCADE |
| `maintenance_records` | `incident_id` | `incidents` | `id` | CASCADE | CASCADE |
| `maintenance_records` | `user_id` | `users` | `id` | RESTRICT | CASCADE |

---

# 12. Enum-Like Values

The database uses `VARCHAR` columns for fields with a predefined set of values.

This approach keeps the values readable while allowing the application to control which values are accepted.

| Table | Column | Allowed Values |
|---|---|---|
| `users` | `user_role` | `GENERAL_USER`, `TECHNICIAN`, `ADMINISTRATOR` |
| `rooms` | `room_type` | `CLASSROOM`, `LABORATORY`, `LECTURE_HALL`, `OFFICE`, `SERVER_ROOM`, `STORAGE`, `WORKSHOP`, `CONFERENCE_ROOM`, `RESTROOM`, `OTHER` |
| `rooms` | `criticality` | `LOW`, `NORMAL`, `HIGH`, `CRITICAL` |
| `assets` | `category` | `COMPUTER`, `PROJECTOR`, `AIRCONDITIONER`, `FURNITURE`, `NETWORK_EQUIPMENT`, `LAB_EQUIPMENT`, `ELECTRICAL_EQUIPMENT`, `OFFICE_EQUIPMENT`, `OTHER` |
| `assets` | `status` | `IN_USE`, `AVAILABLE`, `UNDER_MAINTENANCE`, `OUT_OF_SERVICE`, `RETIRED` |
| `assets` | `asset_condition` | `GOOD`, `FAIR`, `DAMAGED`, `POOR` |
| `assets` | `criticality` | `LOW`, `NORMAL`, `HIGH`, `CRITICAL` |
| `nfc_tags` | `status` | `ACTIVE`, `INACTIVE`, `LOST`, `DAMAGED`, `REPLACED` |
| `incidents` | `category` | `EQUIPMENT_FAILURE`, `ELECTRICAL`, `PLUMBING`, `STRUCTURAL`, `HVAC`, `NETWORK_IT`, `FURNITURE`, `SAFETY_HAZARD`, `OTHER` |
| `incidents` | `priority` | `LOW`, `MEDIUM`, `HIGH`, `CRITICAL` |
| `incidents` | `status` | `REPORTED`, `EVALUATED`, `ASSIGNED`, `IN_PROGRESS`, `RESOLVED`, `VERIFIED`, `CLOSED` |
| `maintenance_records` | `status` | `PENDING`, `IN_PROGRESS`, `COMPLETED`, `CANCELLED` |

---

# 13. Data Integrity Rules

The database uses foreign keys to maintain relationships between records.

The main integrity rules are:

1. Every room must belong to an existing building.
2. A room number must be unique within its building.
3. Every asset must belong to an existing room.
4. Every NFC tag must belong to an existing asset.
5. Every incident must belong to a room.
6. An incident may optionally reference an asset.
7. Every incident must have a reporting user.
8. An incident may optionally have an assigned user.
9. Every maintenance record must belong to an existing incident.
10. Every maintenance record must reference an existing user.
11. User email addresses must be unique.
12. NFC tag UIDs must be unique.
13. Incident numbers must be unique.
14. Primary keys use `BIGINT` with auto-increment.
15. Passwords are stored in `password_hash`, which is intended to contain hashed passwords rather than plaintext passwords.

---

# 14. Incident Reporting and Asset Association

The database supports two main types of incident reports.

### Asset-Related Incident

An incident can reference a specific asset:

```text
Incident
   │
   ├── room_id → Room
   └── asset_id → Asset
```

This can be used when a user reports an issue with equipment such as a computer, projector, air conditioner, or other registered asset.

### Facility-Related Incident

An incident can also exist without an associated asset:

```text
Incident
   │
   ├── room_id → Room
   └── asset_id → NULL
```

This allows the system to handle issues such as plumbing, electrical, structural, or other facility problems where there is no specific asset involved.

---

# 15. Incident Evaluation

The database stores several values that support automatic incident evaluation.

```text
safety_hazard
       │
       ├── operational_impact
       │
       ├── affected_area
       │
       └── asset/room criticality
                    │
                    ▼
             priority_score
                    │
                    ▼
                priority
```

The `priority_score` stores the calculated score, while `priority` stores the resulting priority classification.

The database does not store a priority selected by the reporter as part of the incident input. Instead, the structured incident information provides the data required by the evaluation process.

---

# 16. Incident and Maintenance History

The relationship between incidents and maintenance records allows the system to maintain a history of maintenance activities.

For example:

```text
Incident #INC-001
    │
    ├── Maintenance Record #1
    │      ├── Action
    │      ├── Remarks
    │      └── Status
    │
    ├── Maintenance Record #2
    │      ├── Action
    │      ├── Remarks
    │      └── Status
    │
    └── Maintenance Record #3
           ├── Action
           ├── Remarks
           └── Status
```

This provides a record of the work performed on an incident instead of storing only the incident's final status.

---

# 17. Design Summary

The database is organized around seven main entities:

```text
Users
  │
  ├───────────────┐
  │               │
  ▼               ▼
Incidents    Maintenance Records
  │
  │
  ├── Assets
  │     │
  │     └── NFC Tags
  │
  └── Rooms
        │
        └── Buildings
```

The design separates campus locations, physical assets, incidents, users, and maintenance activities into their own tables. Foreign keys connect these entities while the nullable relationships allow the system to support incidents that do not involve a specific asset.

The schema also provides the data needed for the system's incident lifecycle, automated priority evaluation, asset tracking, NFC identification, technician assignment, and maintenance history.