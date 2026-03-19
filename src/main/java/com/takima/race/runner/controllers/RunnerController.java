package com.takima.race.runner.controllers;

import com.takima.race.runner.entities.Runner;
import com.takima.race.runner.services.RunnerService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/runners")
public class RunnerController {
    private final RunnerService runnerService;

    public RunnerController(RunnerService runnerService) {
        this.runnerService = runnerService;
    }

    @GetMapping
    public List<Runner> getAll() {
        return runnerService.getAll();
    }

    @GetMapping("/{id}")
    public Runner getById(@PathVariable Long id) {
        return runnerService.getById(id);
    }

    @PostMapping
    public Runner create(@RequestBody Runner runner) {
        return runnerService.create(runner);
    }

    @PutMapping("/{id}")
    public Runner update(@PathVariable Long id, @RequestBody Runner runner) {
        runner.setId(id);
        return runnerService.update(runner);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        runnerService.delete(id);
    }
}
