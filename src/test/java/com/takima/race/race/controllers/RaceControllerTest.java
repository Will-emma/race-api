package com.takima.race.race.controllers;

import com.takima.race.race.entities.Race;
import com.takima.race.registration.entities.Registration;
import com.takima.race.race.services.RaceService;
import com.takima.race.registration.services.RegistrationService;
import com.takima.race.runner.entities.Runner;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RaceControllerTest {

    private final RaceService raceService = mock(RaceService.class);
    private final RegistrationService registrationService = mock(RegistrationService.class);
    private final MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(new RaceController(raceService, registrationService))
            .build();

    @Test
    void getAllRacesReturnsRaceList() throws Exception {
        Race race = new Race();
        race.setId(1L);
        race.setName("Semi-marathon de Paris");
        race.setDate(LocalDate.of(2026, 6, 1));
        race.setLocation("Paris");
        race.setMaxParticipants(500);

        when(raceService.getAll(null)).thenReturn(List.of(race));

        mockMvc.perform(get("/races"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Semi-marathon de Paris"))
                .andExpect(jsonPath("$[0].date").value("2026-06-01"))
                .andExpect(jsonPath("$[0].location").value("Paris"))
                .andExpect(jsonPath("$[0].maxParticipants").value(500));

        verify(raceService).getAll(null);
    }

    @Test
    void getAllRacesCanFilterByLocation() throws Exception {
        when(raceService.getAll("Paris")).thenReturn(List.of());

        mockMvc.perform(get("/races").param("location", "Paris"))
                .andExpect(status().isOk());

        verify(raceService).getAll("Paris");
    }

    @Test
    void getRaceByIdReturnsNotFoundWhenRaceDoesNotExist() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Course 99 introuvable"))
                .when(raceService)
                .getById(99L);

        mockMvc.perform(get("/races/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void countParticipantsReturnsCount() throws Exception {
        when(raceService.countParticipants(1L)).thenReturn(42L);

        mockMvc.perform(get("/races/1/participants/count"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.count").value(42));
    }

    @Test
    void countParticipantsReturnsNotFoundWhenRaceDoesNotExist() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Course 99 introuvable"))
                .when(raceService)
                .countParticipants(99L);

        mockMvc.perform(get("/races/99/participants/count"))
                .andExpect(status().isNotFound());
    }

    @Test
    void registerRunnerReturnsCreated() throws Exception {
        when(registrationService.register(eq(1L), eq(7L))).thenReturn(new Registration());

        mockMvc.perform(post("/races/1/registrations")
                        .contentType("application/json")
                        .content("""
                                {
                                  "runnerId": 7
                                }
                                """))
                .andExpect(status().isCreated());
    }

    @Test
    void getParticipantsReturnsRunnerList() throws Exception {
        Runner runner = new Runner();
        runner.setId(7L);
        runner.setFirstName("Alice");
        runner.setLastName("Martin");
        runner.setEmail("alice@example.com");
        runner.setAge(30);

        when(raceService.getParticipants(1L)).thenReturn(List.of(runner));

        mockMvc.perform(get("/races/1/registrations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(7))
                .andExpect(jsonPath("$[0].firstName").value("Alice"))
                .andExpect(jsonPath("$[0].lastName").value("Martin"))
                .andExpect(jsonPath("$[0].email").value("alice@example.com"))
                .andExpect(jsonPath("$[0].age").value(30));
    }

    @Test
    void updateRaceReturnsCreated() throws Exception {
        Race race = new Race();
        race.setId(1L);
        race.setName("Marathon de Lyon");
        race.setDate(LocalDate.of(2026, 9, 20));
        race.setLocation("Lyon");
        race.setMaxParticipants(800);

        when(raceService.update(org.mockito.ArgumentMatchers.any(Race.class))).thenReturn(race);

        mockMvc.perform(put("/races/1")
                        .contentType("application/json")
                        .content("""
                                {
                                  "name": "Marathon de Lyon",
                                  "date": "2026-09-20",
                                  "location": "Lyon",
                                  "maxParticipants": 800
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Marathon de Lyon"))
                .andExpect(jsonPath("$.location").value("Lyon"));
    }

    @Test
    void updateRaceReturnsNotFoundWhenRaceDoesNotExist() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Course 99 introuvable"))
                .when(raceService)
                .update(org.mockito.ArgumentMatchers.any(Race.class));

        mockMvc.perform(put("/races/99")
                        .contentType("application/json")
                        .content("""
                                {
                                  "name": "Marathon de Lyon",
                                  "date": "2026-09-20",
                                  "location": "Lyon",
                                  "maxParticipants": 800
                                }
                                """))
                .andExpect(status().isNotFound());
    }
}
