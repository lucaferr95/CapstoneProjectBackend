package it.epicode.CapstoneProjectBackend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import java.time.LocalDate;

@Entity
@Data
public class Points {
    @Id
    @GeneratedValue
    private int id;
    @Column(nullable = false)
    private int userId;
    @Column(nullable = false)
    private LocalDate lastUpdated;
    @Column(nullable = false)
    private int points;

}
