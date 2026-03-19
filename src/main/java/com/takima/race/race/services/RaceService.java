package com.takima.race.race.services;

import com.takima.race.race.entities.Race;
import com.takima.race.race.repositories.RaceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RaceService {

    private final RaceRepository raceRepository;

    public RaceService(RaceRepository raceRepository) {
        this.raceRepository = raceRepository;
    }

    public List<Race> getAll() {
        return raceRepository.findAll();
    }

    public Race getById(Long id) {
        return raceRepository.findById(id).orElse(null);
    }   

    public Race create(Race race) {
        return raceRepository.save(race);
    }

    public long countParticipants(Long raceId) {
        if (!raceRepository.existsById(raceId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("Race %s not found", raceId)
            );
        }
        return raceRepository.countParticipantsByRaceId(raceId);
    }
}
