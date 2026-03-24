package com.takima.race.runner.controllers;

import com.takima.race.race.entities.Race;
import com.takima.race.registration.services.RegistrationService;
import com.takima.race.runner.entities.Runner;
import com.takima.race.runner.services.RunnerService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class RunnerControllerTest {

    private final RunnerService runnerService = mock(RunnerService.class);
    private final RegistrationService registrationService = mock(RegistrationService.class);
    private final MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(new RunnerController(runnerService, registrationService))
            .build();

    @Test
    void deleteRunnerReturnsOkWhenRunnerExists() throws Exception {
        doNothing().when(runnerService).delete(1L);

        mockMvc.perform(delete("/runners/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteRunnerReturnsNotFoundWhenRunnerDoesNotExist() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Coureur 99 introuvable"))
                .when(runnerService)
                .delete(99L);

        mockMvc.perform(delete("/runners/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getRacesByRunnerReturnsRaceList() throws Exception {
        Race race = new Race();
        race.setId(1L);
        race.setName("Semi-marathon de Paris");
        race.setDate(LocalDate.of(2026, 6, 1));
        race.setLocation("Paris");
        race.setMaxParticipants(500);

        when(registrationService.getRacesByRunner(1L)).thenReturn(List.of(race));

        mockMvc.perform(get("/runners/1/races"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Semi-marathon de Paris"))
                .andExpect(jsonPath("$[0].date").value("2026-06-01"))
                .andExpect(jsonPath("$[0].location").value("Paris"))
                .andExpect(jsonPath("$[0].maxParticipants").value(500));
    }

    @Test
    void createRunnerReturnsCreated() throws Exception {
        Runner runner = new Runner();
        runner.setId(1L);
        runner.setFirstName("Alice");
        runner.setLastName("Martin");
        runner.setEmail("alice@example.com");
        runner.setAge(30);

        when(runnerService.create(org.mockito.ArgumentMatchers.any(Runner.class))).thenReturn(runner);

        mockMvc.perform(post("/runners")
                        .contentType("application/json")
                        .content("""
                                {
                                  "firstName": "Alice",
                                  "lastName": "Martin",
                                  "email": "alice@example.com",
                                  "age": 30
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void createRunnerReturnsBadRequestWhenEmailIsInvalid() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'email du coureur doit contenir @"))
                .when(runnerService)
                .create(org.mockito.ArgumentMatchers.any(Runner.class));

        mockMvc.perform(post("/runners")
                        .contentType("application/json")
                        .content("""
                                {
                                  "firstName": "Alice",
                                  "lastName": "Martin",
                                  "email": "alice.example.com",
                                  "age": 30
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateRunnerReturnsCreated() throws Exception {
        Runner runner = new Runner();
        runner.setId(1L);
        runner.setFirstName("Alice");
        runner.setLastName("Martin");
        runner.setEmail("alice@example.com");
        runner.setAge(31);

        when(runnerService.update(org.mockito.ArgumentMatchers.any(Runner.class))).thenReturn(runner);

        mockMvc.perform(put("/runners/1")
                        .contentType("application/json")
                        .content("""
                                {
                                  "firstName": "Alice",
                                  "lastName": "Martin",
                                  "email": "alice@example.com",
                                  "age": 31
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.age").value(31));
    }
}
