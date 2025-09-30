package com.fiap.eca.api_marcacao_consultas.controller;

import com.fiap.eca.api_marcacao_consultas.model.Reading;
import com.fiap.eca.api_marcacao_consultas.repository.ReadingRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/readings")
public class ReadingController {

    private final ReadingRepository repo;

    public ReadingController(ReadingRepository repo) {
        this.repo = repo;
    }

    // 🔎 Lista todas ou últimas leituras de um sensor
    @GetMapping
    public List<Reading> getReadings(@RequestParam(required = false) String sensorId) {
        if (sensorId != null) {
            return repo.findTop100BySensorIdOrderByTimestampDesc(sensorId);
        }
        return repo.findAll();
    }

    // ➕ Cria uma nova leitura
    @PostMapping
    public Reading createReading(@RequestBody Reading reading) {
        return repo.save(reading);
    }
}