package com.takima.race.race.services;

import com.takima.race.race.entities.Race;
import com.takima.race.race.repositories.RaceRepository;
import com.takima.race.registration.repositories.RegistrationRepository;
import com.takima.race.runner.entities.Runner;
import com.takima.race.runner.repositories.RunnerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RaceService {

    private final RaceRepository raceRepository;
    private final RegistrationRepository registrationRepository;
    private final RunnerRepository runnerRepository;

    public RaceService(
            RaceRepository raceRepository,
            RegistrationRepository registrationRepository,
            RunnerRepository runnerRepository
    ) {
        this.raceRepository = raceRepository;
        this.registrationRepository = registrationRepository;
        this.runnerRepository = runnerRepository;
    }

    public List<Race> getAll(String location) {
        // Si aucun filtre n'est fourni, on renvoie simplement toutes les courses.
        if (location == null || location.isBlank()) {
            return raceRepository.findAll();
        }

        // Sinon on applique le filtre optionnel demandé dans le bonus du sujet.
        return raceRepository.findByLocationIgnoreCase(location);
    }

    public Race getById(Long id) {
        // Ici on renvoie 404 explicitement pour respecter le contrat du TP.
        return raceRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Course %s introuvable", id)
                )
        );
    }

    public Race create(Race race) {
        return raceRepository.save(race);
    }

    public Race update(Race race) {
        // On refuse la modification si l'id n'existe pas deja en base.
        if (race.getId() == null || !raceRepository.existsById(race.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("Course %s introuvable", race.getId())
            );
        }
        return raceRepository.save(race);
    }

    public long countParticipants(Long raceId) {
        // On vérifie d'abord que la course existe pour éviter de compter sur une ressource absente.
        getById(raceId);
        return registrationRepository.countByRaceId(raceId);
    }

    public List<Runner> getParticipants(Long raceId) {
        // On force un 404 si la course n'existe pas avant de chercher ses participants.
        getById(raceId);

        // On récupère les ids inscrits, puis on charge les coureurs correspondants.
        List<Long> runnerIds = registrationRepository.findByRaceId(raceId).stream()
                .map(registration -> registration.getRunnerId())
                .toList();

        // Si aucun coureur n'est inscrit, on renvoie une liste vide.
        if (runnerIds.isEmpty()) {
            return List.of();
        }

        // On garde l'ordre des inscriptions pour que la réponse soit plus prévisible.
        return runnerIds.stream()
                .map(runnerId -> runnerRepository.findById(runnerId).orElse(null))
                .filter(runner -> runner != null)
                .collect(Collectors.toList());
    }
}
