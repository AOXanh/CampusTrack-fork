# Campus Asset & Incident Management System

A platform that links physical campus assets to their digital records using NFC, so reporting and tracking maintenance issues becomes a lot less painful.

## Project Structure
```
CampusTrack/
├── backend/          # Java Spring Boot core with Python microservices (API, auth, business logic)
├── database/         # MySQL Dockerfile and schema/migration scripts
├── frontend/          # Next.js web application
└── README.md
```

## Why This Exists

Right now, reporting a broken AC unit or a safety hazard on campus is usually messy. There's no clear history for individual assets, no consistent way to figure out what's actually urgent, and almost no visibility into who's working on what.

## How It Works

Tap an NFC-tagged asset with your phone and you'll instantly pull up its live record. If there's no tagged asset involved (say, a leaking ceiling in a hallway), you can still file a manual report by selecting the building and room.

Instead of letting reporters guess a priority level, the system asks for the actual facts: is there a safety hazard, is it affecting operations, how critical is the asset involved. A rule-based engine then calculates the priority on its own. Certain things, like exposed wiring, gas leaks, or fire and smoke, automatically get flagged as Critical no matter what.

## Features

- Asset management for registering and tracking campus assets
- NFC-based asset identification (tap to view an asset's record)
- Incident reporting through NFC or manual building/room selection
- Automated priority evaluation instead of manual assignment
- Maintenance tracking, including assignment and logging of repair work
- Full incident and maintenance history for every asset
- Role-based access so each user type sees only what they need
- An admin dashboard for managing assets, locations, incidents, and users

## Incident Lifecycle

Reported, then Evaluated, then Assigned, then In Progress, then Resolved, then Verified, and finally Closed.

## Roles

General users, meaning students and faculty, can report issues, use NFC, and view their own reports. Technicians can view assigned incidents, carry out maintenance, and log what they did. Administrators manage assets, locations, incidents, users, and assignments across the whole system.

## Tech Stack

The backend runs on Java with Spring Boot, moving toward Python microservices. Both share a single MySQL database. Authentication uses Spring Security with JWT, and the token scheme is shared across services so login works consistently. The frontend is built with HTML, CSS, and JavaScript. On the hardware side, it relies on NFC tags paired with an NFC-enabled mobile device.

## What's In Scope

Asset registration, building and room management, NFC identification, incident reporting through both NFC and manual entry, automated priority evaluation, technician assignment, role-based authentication, and complete history tracking.

---

*Built as an OOPROG special project.*
