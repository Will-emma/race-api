package com.takima.race.registration.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "runner_id")
    private Long runnerId;

    @Column(name = "race_id")
    private Long raceId;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Registration that = (Registration) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public Long getId() { return id; }
    public Long getRunnerId() { return runnerId; }
    public Long getRaceId() { return raceId; }
    public LocalDate getRegistrationDate() { return registrationDate; }

    public void setId(Long id) { this.id = id; }
    public void setRunnerId(Long runnerId) { this.runnerId = runnerId; }
    public void setRaceId(Long raceId) { this.raceId = raceId; }
    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }
}
