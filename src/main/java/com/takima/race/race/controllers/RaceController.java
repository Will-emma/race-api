package com.takima.race.race.controllers;

import com.takima.race.race.entities.Race;
import com.takima.race.race.services.RaceService;
import com.takima.race.registration.controllers.CreateRegistrationRequest;
import com.takima.race.registration.services.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/races")
public class RaceController {

    private final RaceService raceService;
    private final RegistrationService registrationService;

    public RaceController(RaceService raceService, RegistrationService registrationService) {
        this.raceService = raceService;
        this.registrationService = registrationService;
    }

    @GetMapping
    public List<Race> getAll(@RequestParam(required = false) String location) {
        // Le paramètre location reste optionnel: sans lui, on renvoie toutes les courses.
        return raceService.getAll(location);
    }

    @GetMapping("/{id}")
    public Race getById(@PathVariable Long id) {
        return raceService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Race create(@RequestBody Race race) {
        return raceService.create(race);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Race update(@PathVariable Long id, @RequestBody Race race) {
        // On recopie l'id de l'URL dans l'objet pour cibler exactement la course a modifier.
        race.setId(id);
        return raceService.update(race);
    }

    @GetMapping("/{raceId}/participants/count")
    public ParticipantCountResponse countParticipants(@PathVariable Long raceId) {
        return new ParticipantCountResponse(raceService.countParticipants(raceId));
    }

    @PostMapping("/{raceId}/registrations")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerRunner(@PathVariable Long raceId, @RequestBody CreateRegistrationRequest request) {
        registrationService.register(raceId, request.runnerId());
    }

    @GetMapping("/{raceId}/registrations")
    public List<com.takima.race.runner.entities.Runner> getParticipants(@PathVariable Long raceId) {
        // Cet endpoint renvoie les coureurs inscrits à une course donnée.
        return raceService.getParticipants(raceId);
    }

}
