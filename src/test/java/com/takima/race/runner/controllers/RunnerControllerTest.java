package com.takima.race.runner.controllers;

import com.takima.race.runner.services.RunnerService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RunnerControllerTest {

    private final RunnerService runnerService = mock(RunnerService.class);
    private final MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(new RunnerController(runnerService))
            .build();

    @Test
    void deleteRunnerReturnsOkWhenRunnerExists() throws Exception {
        doNothing().when(runnerService).delete(1L);

        mockMvc.perform(delete("/runners/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteRunnerReturnsNotFoundWhenRunnerDoesNotExist() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Runner 99 not found"))
                .when(runnerService)
                .delete(99L);

        mockMvc.perform(delete("/runners/99"))
                .andExpect(status().isNotFound());
    }
}
