package com.takima.race.registration.services;

import com.takima.race.race.repositories.RaceRepository;
import com.takima.race.registration.entities.Registration;
import com.takima.race.registration.repositories.RegistrationRepository;
import com.takima.race.runner.repositories.RunnerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final RaceRepository raceRepository;
    private final RunnerRepository runnerRepository;

    public RegistrationService(
            RegistrationRepository registrationRepository,
            RaceRepository raceRepository,
            RunnerRepository runnerRepository
    ) {
        this.registrationRepository = registrationRepository;
        this.raceRepository = raceRepository;
        this.runnerRepository = runnerRepository;
    }

    public Registration register(Long raceId, Long runnerId) {
        if (!raceRepository.existsById(raceId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("Race %s not found", raceId)
            );
        }
        if (!runnerRepository.existsById(runnerId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("Runner %s not found", runnerId)
            );
        }

        Registration registration = new Registration();
        registration.setRaceId(raceId);
        registration.setRunnerId(runnerId);
        registration.setRegistrationDate(LocalDate.now());
        return registrationRepository.save(registration);
    }
}
