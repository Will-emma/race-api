package com.takima.race.race.controllers;

import com.takima.race.race.entities.Race;
import com.takima.race.race.services.RaceService;
import com.takima.race.registration.controllers.CreateRegistrationRequest;
import com.takima.race.registration.services.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public List<Race> getAll() {
        return raceService.getAll();
    }

    @GetMapping("/{id}")
    public Race getById(@PathVariable Long id) {
        return raceService.getById(id);
    }

    @PostMapping
    public Race create(@RequestBody Race race) {
        return raceService.create(race);
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

}
