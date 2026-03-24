package com.takima.race.registration.repositories;

import com.takima.race.registration.entities.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    boolean existsByRunnerIdAndRaceId(Long runnerId, Long raceId);

    long countByRaceId(Long raceId);

    List<Registration> findByRaceId(Long raceId);

    List<Registration> findByRunnerId(Long runnerId);
}
