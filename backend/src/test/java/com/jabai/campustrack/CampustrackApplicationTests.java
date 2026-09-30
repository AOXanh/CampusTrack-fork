package com.jabai.campustrack;

import com.jabai.campustrack.Models.*;
import com.jabai.campustrack.Models.Enums.*;
import com.jabai.campustrack.Repositories.IncidentRepository;
import com.jabai.campustrack.Securities.JwtUtil;
import com.jayway.jsonpath.JsonPath;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Exercise real JWT authentication, DTO validation, services, repositories, and migrations.
@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:campustrack_test;MODE=MySQL;DB_CLOSE_DELAY=-1",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa", "spring.datasource.password=",
    "spring.jwt.secret=01234567890123456789012345678901",
    "spring.jpa.show-sql=false"
})
@AutoConfigureMockMvc
@Transactional
class CampustrackApplicationTests {
    @Autowired MockMvc mvc;
    @Autowired EntityManager entities;
    @Autowired JwtUtil jwt;
    @Autowired IncidentRepository incidents;
    private User reporter, otherReporter, admin, worker, otherWorker;
    private Room room, otherRoom;
    private Asset asset;

    @BeforeEach
    void fixtures() {
        Building building = persist(new Building("Test building"));
        room = persist(new Room(building, "101", 30, RoomType.CLASSROOM, RoomCriticality.LOW));
        otherRoom = persist(new Room(building, "102", 30, RoomType.CLASSROOM, RoomCriticality.NORMAL));
        asset = persist(new Asset(room, "Projector", AssetCategory.PROJECTOR,
            AssetStatus.IN_USE, AssetCondition.GOOD, AssetCriticality.HIGH));
        reporter = persist(new User("Reporter", "reporter@test.edu", "test-hash", UserRole.USER));
        otherReporter = persist(new User("Other reporter", "other@test.edu", "test-hash", UserRole.USER));
        admin = persist(new User("Admin", "admin@test.edu", "test-hash", UserRole.ADMIN));
        worker = persist(new User("Worker", "worker@test.edu", "test-hash", UserRole.WORKER));
        otherWorker = persist(new User("Other worker", "otherworker@test.edu", "test-hash", UserRole.WORKER));
    }

    @Test
    void authenticatedCrudPersistsReportsAndReturnsDtos() throws Exception {
        long id = create(reporter, report(room.getId(), null, false, false, 1));
        mvc.perform(get("/api/incidents/{id}", id).header("Authorization", token(reporter)))
            .andExpect(status().isOk()).andExpect(jsonPath("$.reportedById").value(reporter.getId()))
            .andExpect(jsonPath("$.status").value("EVALUATED"))
            .andExpect(jsonPath("$.priority").value("LOW"))
            .andExpect(jsonPath("$.reportedBy").doesNotExist())
            .andExpect(jsonPath("$.hashedPassword").doesNotExist());
        mvc.perform(put("/api/incidents/{id}", id).header("Authorization", token(admin))
                .contentType("application/json").content(report(room.getId(), null, true, true, 2)))
            .andExpect(status().isOk()).andExpect(jsonPath("$.priority").value("CRITICAL"));
        entities.flush();
        entities.clear();
        assertThat(incidents.findById(id).orElseThrow().getSafetyHazard()).isTrue();
        mvc.perform(delete("/api/incidents/{id}", id).header("Authorization", token(admin)))
            .andExpect(status().isNoContent());
        entities.flush();
        assertThat(incidents.existsById(id)).isFalse();
        mvc.perform(get("/api/incidents/{id}", id).header("Authorization", token(admin)))
            .andExpect(status().isNotFound()).andExpect(jsonPath("$.message").value("Incident not found."));
    }

    @Test
    void incidentRoutesRequireValidJwt() throws Exception {
        mvc.perform(get("/api/incidents")).andExpect(status().isUnauthorized());
        mvc.perform(post("/api/incidents").contentType("application/json")
                .content(report(room.getId(), null, false, false, 1)))
            .andExpect(status().isUnauthorized());
        mvc.perform(get("/api/incidents").header("Authorization", "Bearer invalid"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void validatesReportFieldsAndLocationReferences() throws Exception {
        String valid = report(room.getId(), null, false, false, 1);
        for (String body : new String[] {"{}", valid.replace("Broken projector", " "),
                report(null, null, false, false, 1), report(room.getId(), null, false, false, 0),
                valid.replace("EQUIPMENT_FAILURE", "INVALID_CATEGORY"), "{"}) {
            mvc.perform(post("/api/incidents").header("Authorization", token(reporter))
                    .contentType("application/json").content(body))
                .andExpect(status().isBadRequest());
        }
        mvc.perform(post("/api/incidents").header("Authorization", token(reporter))
                .contentType("application/json").content(report(Long.MAX_VALUE, null, false, false, 1)))
            .andExpect(status().isNotFound());
        mvc.perform(post("/api/incidents").header("Authorization", token(reporter))
                .contentType("application/json").content(report(room.getId(), Long.MAX_VALUE, false, false, 1)))
            .andExpect(status().isNotFound());
        mvc.perform(post("/api/incidents").header("Authorization", token(reporter))
                .contentType("application/json").content(report(otherRoom.getId(), asset.getId(), false, false, 1)))
            .andExpect(status().isBadRequest());
        assertThat(incidents.count()).isZero();
    }

    @Test
    void assetReportsDeriveRoomAndCannotOverrideServerFields() throws Exception {
        String body = report(null, asset.getId(), false, false, 1).replace("}",
            ",\"reportedById\":" + admin.getId() + ",\"priority\":\"CRITICAL\",\"status\":\"CLOSED\"}");
        long id = create(reporter, body);
        mvc.perform(get("/api/incidents/{id}", id).header("Authorization", token(reporter)))
            .andExpect(status().isOk()).andExpect(jsonPath("$.roomId").value(room.getId()))
            .andExpect(jsonPath("$.assetId").value(asset.getId()))
            .andExpect(jsonPath("$.reportedById").value(reporter.getId()))
            .andExpect(jsonPath("$.priority").value("MEDIUM"))
            .andExpect(jsonPath("$.status").value("EVALUATED"));
    }

    @ParameterizedTest
    @CsvSource({"false,false,1,LOW,1", "false,true,1,MEDIUM,4", "false,true,100,HIGH,12", "true,false,1,CRITICAL,14"})
    void priorityUsesFactsAndStoredCriticality(boolean hazard, boolean impact, int area,
                                             String priority, int score) throws Exception {
        if (area == 100) room.setCriticality(RoomCriticality.CRITICAL);
        long id = create(reporter, report(room.getId(), null, hazard, impact, area));
        mvc.perform(get("/api/incidents/{id}", id).header("Authorization", token(reporter)))
            .andExpect(status().isOk()).andExpect(jsonPath("$.priority").value(priority))
            .andExpect(jsonPath("$.priorityScore").value(score));
    }

    @Test
    void listsAndReadsRespectOwnershipAndAssignment() throws Exception {
        long own = create(reporter, report(room.getId(), null, false, false, 1));
        long other = create(otherReporter, report(room.getId(), null, false, false, 1));
        changeStatus(other, admin, "ASSIGNED", worker.getId(), 200);
        mvc.perform(get("/api/incidents").header("Authorization", token(reporter)))
            .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].id").value(own));
        mvc.perform(get("/api/incidents").header("Authorization", token(worker)))
            .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].id").value(other));
        mvc.perform(get("/api/incidents/{id}", other).header("Authorization", token(worker)))
            .andExpect(status().isOk());
        mvc.perform(get("/api/incidents/{id}", other).header("Authorization", token(reporter)))
            .andExpect(status().isForbidden());
        mvc.perform(get("/api/incidents/{id}", own).header("Authorization", token(otherWorker)))
            .andExpect(status().isForbidden());
        mvc.perform(get("/api/incidents").param("size", "1").header("Authorization", token(admin)))
            .andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(other));
        mvc.perform(get("/api/incidents").param("page", "1").param("size", "1")
                .header("Authorization", token(admin)))
            .andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(own));
        mvc.perform(get("/api/incidents").param("size", "101").header("Authorization", token(admin)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void administratorsControlEditingAndDeletion() throws Exception {
        long id = create(reporter, report(room.getId(), null, false, false, 1));
        for (User actor : new User[] {reporter, worker}) {
            mvc.perform(put("/api/incidents/{id}", id).header("Authorization", token(actor))
                    .contentType("application/json").content(report(room.getId(), null, false, false, 1)))
                .andExpect(status().isForbidden());
            mvc.perform(delete("/api/incidents/{id}", id).header("Authorization", token(actor)))
                .andExpect(status().isForbidden());
        }
    }

    @Test
    void lifecycleRequiresValidAssignmentRolesAndTransitions() throws Exception {
        long id = create(reporter, report(room.getId(), null, false, false, 1));
        changeStatus(id, admin, "CLOSED", null, 409);
        changeStatus(id, reporter, "ASSIGNED", worker.getId(), 403);
        changeStatus(id, admin, "ASSIGNED", null, 400);
        changeStatus(id, admin, "ASSIGNED", reporter.getId(), 400);
        changeStatus(id, admin, "ASSIGNED", Long.MAX_VALUE, 404);
        changeStatus(id, admin, "ASSIGNED", worker.getId(), 200);
        changeStatus(id, otherWorker, "IN_PROGRESS", null, 403);
        changeStatus(id, worker, "IN_PROGRESS", worker.getId(), 400);
        changeStatus(id, worker, "IN_PROGRESS", null, 200);
        changeStatus(id, worker, "RESOLVED", null, 200);
        changeStatus(id, worker, "VERIFIED", null, 403);
        changeStatus(id, admin, "VERIFIED", null, 200);
        changeStatus(id, admin, "CLOSED", null, 200);
        mvc.perform(get("/api/incidents/{id}", id).header("Authorization", token(admin)))
            .andExpect(status().isOk()).andExpect(jsonPath("$.resolvedAt").exists())
            .andExpect(jsonPath("$.closedAt").exists());
        changeStatus(id, admin, "REPORTED", null, 409);
        mvc.perform(delete("/api/incidents/{id}", id).header("Authorization", token(admin)))
            .andExpect(status().isConflict());
    }

    @Test
    void maintenanceHistoryPreventsEditsAndCascadeDeletion() throws Exception {
        long id = create(reporter, report(room.getId(), null, false, false, 1));
        Incident incident = incidents.findById(id).orElseThrow();
        incident.addMaintenanceRecord(new MaintenanceRecord(incident, worker,
            "Inspection", null, MaintenanceRecordStatus.PENDING));
        entities.flush();
        entities.clear();
        mvc.perform(delete("/api/incidents/{id}", id).header("Authorization", token(admin)))
            .andExpect(status().isConflict());
        mvc.perform(put("/api/incidents/{id}", id).header("Authorization", token(admin))
                .contentType("application/json").content(report(room.getId(), null, false, false, 1)))
            .andExpect(status().isConflict());
        assertThat(incidents.findById(id).orElseThrow().getMaintenanceRecords()).hasSize(1);
    }

    private <T> T persist(T entity) { entities.persist(entity); return entity; }
    private String token(User user) { return "Bearer " + jwt.generateToken(user); }

    private long create(User actor, String body) throws Exception {
        String json = mvc.perform(post("/api/incidents").header("Authorization", token(actor))
                .contentType("application/json").content(body))
            .andExpect(status().isCreated()).andExpect(header().exists("Location"))
            .andExpect(jsonPath("$.createdAt").exists()).andExpect(jsonPath("$.evaluatedAt").exists())
            .andReturn().getResponse().getContentAsString();
        return ((Number) JsonPath.read(json, "$.id")).longValue();
    }

    private void changeStatus(long id, User actor, String state, Long assignee, int expected) throws Exception {
        mvc.perform(patch("/api/incidents/{id}/status", id).header("Authorization", token(actor))
                .contentType("application/json")
                .content("{\"status\":\"" + state + "\",\"assignedToId\":" + assignee + "}"))
            .andExpect(status().is(expected));
    }

    private String report(Long roomId, Long assetId, boolean hazard, boolean impact, int area) {
        return """
            {"roomId":%s,"assetId":%s,"category":"EQUIPMENT_FAILURE","description":"Broken projector",
             "safetyHazard":%s,"operationalImpact":%s,"affectedArea":%s}
            """.formatted(roomId, assetId, hazard, impact, area);
    }
}
