package com.takima.race.runner.services;

import com.takima.race.runner.entities.Runner;
import com.takima.race.runner.repositories.RunnerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RunnerService {

    private final RunnerRepository runnerRepository;

    public RunnerService(RunnerRepository runnerRepository) {
        this.runnerRepository = runnerRepository;
    }

    public List<Runner> getAll() {
        return runnerRepository.findAll();
    }

    public Runner getById(Long id) {
        return runnerRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Coureur %s introuvable", id)
                )
        );
    }

    public Runner create(Runner runner) {
        // On valide l'email avant toute sauvegarde pour respecter la règle métier du sujet.
        validateEmail(runner);
        return runnerRepository.save(runner);
    }

    public Runner update(Runner runner) {
        if (runner.getId() == null || !runnerRepository.existsById(runner.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("Coureur %s introuvable", runner.getId())
            );
        }

        // On applique la même validation en modification pour éviter d'enregistrer un email invalide.
        validateEmail(runner);
        return runnerRepository.save(runner);
    }

    public void delete(Long id) {
        if (!runnerRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("Coureur %s introuvable", id)
            );
        }
        runnerRepository.deleteById(id);
    }

    private void validateEmail(Runner runner) {
        if (runner.getEmail() == null || !runner.getEmail().contains("@")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "L'email du coureur doit contenir @"
            );
        }
    }
}
