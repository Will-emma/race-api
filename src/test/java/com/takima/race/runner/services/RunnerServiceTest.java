package com.takima.race.runner.services;

import com.takima.race.runner.entities.Runner;
import com.takima.race.runner.repositories.RunnerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RunnerServiceTest {

    private final RunnerRepository runnerRepository = mock(RunnerRepository.class);
    private final RunnerService runnerService = new RunnerService(runnerRepository);

    @Test
    void createRejectsInvalidEmail() {
        Runner runner = new Runner();
        runner.setEmail("alice.example.com");

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> runnerService.create(runner)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void updateRejectsInvalidEmail() {
        Runner runner = new Runner();
        runner.setId(1L);
        runner.setEmail("alice.example.com");

        when(runnerRepository.existsById(1L)).thenReturn(true);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> runnerService.update(runner)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void getByIdReturnsNotFoundWhenRunnerDoesNotExist() {
        when(runnerRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> runnerService.getById(99L)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void deleteRemovesRunnerWhenItExists() {
        when(runnerRepository.existsById(1L)).thenReturn(true);

        runnerService.delete(1L);

        verify(runnerRepository).deleteById(1L);
    }
}
