package com.takima.race.runner.controllers;

import com.takima.race.race.entities.Race;
import com.takima.race.registration.services.RegistrationService;
import com.takima.race.runner.entities.Runner;
import com.takima.race.runner.services.RunnerService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/runners")
public class RunnerController {
    private final RunnerService runnerService;
    private final RegistrationService registrationService;

    public RunnerController(RunnerService runnerService, RegistrationService registrationService) {
        this.runnerService = runnerService;
        this.registrationService = registrationService;
    }

    @GetMapping
    public List<Runner> getAll() {
        return runnerService.getAll();
    }

    @GetMapping("/{id}")
    public Runner getById(@PathVariable Long id) {
        return runnerService.getById(id);
    }

    @GetMapping("/{runnerId}/races")
    public List<Race> getRaces(@PathVariable Long runnerId) {
        // Cet endpoint renvoie toutes les courses auxquelles un coureur est inscrit.
        return registrationService.getRacesByRunner(runnerId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Runner create(@RequestBody Runner runner) {
        return runnerService.create(runner);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Runner update(@PathVariable Long id, @RequestBody Runner runner) {
        // On recopie l'id de l'URL dans l'objet pour imposer la ressource à modifier.
        runner.setId(id);
        return runnerService.update(runner);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        runnerService.delete(id);
    }
}
