package com.takima.race.registration.services;

import com.takima.race.race.entities.Race;
import com.takima.race.race.repositories.RaceRepository;
import com.takima.race.registration.repositories.RegistrationRepository;
import com.takima.race.runner.entities.Runner;
import com.takima.race.runner.repositories.RunnerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RegistrationServiceTest {

    private final RegistrationRepository registrationRepository = mock(RegistrationRepository.class);
    private final RaceRepository raceRepository = mock(RaceRepository.class);
    private final RunnerRepository runnerRepository = mock(RunnerRepository.class);
    private final RegistrationService registrationService = new RegistrationService(
            registrationRepository,
            raceRepository,
            runnerRepository
    );

    @Test
    void registerReturnsNotFoundWhenRaceDoesNotExist() {
        when(raceRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> registrationService.register(1L, 2L)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void registerReturnsNotFoundWhenRunnerDoesNotExist() {
        Race race = new Race();
        race.setId(1L);
        race.setMaxParticipants(10);

        when(raceRepository.findById(1L)).thenReturn(Optional.of(race));
        when(runnerRepository.findById(2L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> registrationService.register(1L, 2L)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void registerReturnsConflictWhenRunnerAlreadyRegistered() {
        Race race = new Race();
        race.setId(1L);
        race.setMaxParticipants(10);

        Runner runner = new Runner();
        runner.setId(2L);

        when(raceRepository.findById(1L)).thenReturn(Optional.of(race));
        when(runnerRepository.findById(2L)).thenReturn(Optional.of(runner));
        when(registrationRepository.existsByRunnerIdAndRaceId(2L, 1L)).thenReturn(true);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> registrationService.register(1L, 2L)
        );

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
    }

    @Test
    void registerReturnsConflictWhenRaceIsFull() {
        Race race = new Race();
        race.setId(1L);
        race.setMaxParticipants(1);

        Runner runner = new Runner();
        runner.setId(2L);

        when(raceRepository.findById(1L)).thenReturn(Optional.of(race));
        when(runnerRepository.findById(2L)).thenReturn(Optional.of(runner));
        when(registrationRepository.existsByRunnerIdAndRaceId(2L, 1L)).thenReturn(false);
        when(registrationRepository.countByRaceId(1L)).thenReturn(1L);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> registrationService.register(1L, 2L)
        );

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
    }
}
