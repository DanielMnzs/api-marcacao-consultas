package com.fiap.eca.api_marcacao_consultas.repository;

import com.fiap.eca.api_marcacao_consultas.model.Reading;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReadingRepository extends JpaRepository<Reading, Long> {

    // método customizado para buscar as últimas 100 leituras de um sensor
    List<Reading> findTop100BySensorIdOrderByTimestampDesc(String sensorId);
}
