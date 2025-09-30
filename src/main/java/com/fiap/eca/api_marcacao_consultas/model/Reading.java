package com.fiap.eca.api_marcacao_consultas.model;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "readings")  // garante que o nome da tabela seja exatamente esse
public class Reading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sensorId;

    @Column(name = "valor")   // <-- muda o nome da coluna no banco
    private Double value;

    private LocalDateTime timestamp = LocalDateTime.now();

    public Reading() {}

    public Reading(String sensorId, Double value) {
        this.sensorId = sensorId;
        this.value = value;
    }

    // getters e setters
}
