package com.takima.race.registration.services;

import com.takima.race.race.entities.Race;
import com.takima.race.race.repositories.RaceRepository;
import com.takima.race.registration.entities.Registration;
import com.takima.race.registration.repositories.RegistrationRepository;
import com.takima.race.runner.repositories.RunnerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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
        // On charge la course et le coureur tout de suite pour centraliser les 404.
        Race race = raceRepository.findById(raceId).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Course %s introuvable", raceId)
                )
        );
        runnerRepository.findById(runnerId).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Coureur %s introuvable", runnerId)
                )
        );

        // Un même coureur ne peut pas être inscrit deux fois à la même course.
        if (registrationRepository.existsByRunnerIdAndRaceId(runnerId, raceId)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    String.format("Le coureur %s est deja inscrit a la course %s", runnerId, raceId)
            );
        }

        // On refuse l'inscription si la course a déjà atteint sa capacité maximale.
        if (registrationRepository.countByRaceId(raceId) >= race.getMaxParticipants()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    String.format("La course %s est complete", raceId)
            );
        }

        Registration registration = new Registration();
        registration.setRaceId(raceId);
        registration.setRunnerId(runnerId);
        registration.setRegistrationDate(LocalDate.now());
        return registrationRepository.save(registration);
    }

    public List<Race> getRacesByRunner(Long runnerId) {
        // On valide d'abord que le coureur existe, même s'il n'a encore aucune course.
        runnerRepository.findById(runnerId).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Coureur %s introuvable", runnerId)
                )
        );

        List<Long> raceIds = registrationRepository.findByRunnerId(runnerId).stream()
                .map(registration -> registration.getRaceId())
                .toList();

        if (raceIds.isEmpty()) {
            return List.of();
        }

        // On retourne ensuite les courses associées aux inscriptions du coureur.
        return raceIds.stream()
                .map(raceId -> raceRepository.findById(raceId).orElse(null))
                .filter(race -> race != null)
                .collect(Collectors.toList());
    }
}
