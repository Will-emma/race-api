package com.takima.race.race.repositories;

import com.takima.race.race.entities.Race;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RaceRepository extends JpaRepository<Race, Long> {

    @Query(value = "SELECT COUNT(*) FROM registration WHERE race_id = :raceId", nativeQuery = true)
    long countParticipantsByRaceId(@Param("raceId") Long raceId);
}
